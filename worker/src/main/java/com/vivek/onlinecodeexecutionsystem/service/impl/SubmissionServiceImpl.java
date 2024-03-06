package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.SubmissionDao;
import com.vivek.onlinecodeexecutionsystem.model.Status;
import com.vivek.onlinecodeexecutionsystem.model.Submission;
import com.vivek.onlinecodeexecutionsystem.service.SubmissionService;
import com.vivek.onlinecodeexecutionsystem.utility.RedisUtils;
import com.vivek.onlinecodeexecutionsystem.utility.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.vivek.onlinecodeexecutionsystem.constants.DirectoryConstants.*;
import static com.vivek.onlinecodeexecutionsystem.constants.ExecutionConfig.MAX_WALL_TIME_LIMIT;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final Logger LOGGER = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionDao submissionDao;
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${stream.key}")
    private String streamKey;

    @Value("${stream.key.group-name}")
    private String streamGroupName;

    @Autowired
    public SubmissionServiceImpl(SubmissionDao submissionDao, StringRedisTemplate stringRedisTemplate) {
        this.submissionDao = submissionDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void executeSubmission(StringRecord record) {
        long submissionId = Long.parseLong(record.getValue().get("submissionId"));
        LOGGER.info("Executing submissionId:{}", submissionId);
        Optional<Submission> optionalSubmission = submissionDao.findById(submissionId);
        if (optionalSubmission.isEmpty()) {
            LOGGER.warn("Deleting submission id:{} since it does not exist in DB", submissionId);
            RedisUtils.acknowledge(stringRedisTemplate, streamKey, streamGroupName, record.getId());
            RedisUtils.deleteFromStream(stringRedisTemplate, streamKey, record.getId());
            return;
        }
        Submission submission = optionalSubmission.get();
        submission.setStatus(Status.PROCESS);
        //TODO: set hostname
        try {
            submission.setExecutionHost(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            submission.setExecutionHost("UNKNOWN");
        }
        submission = submissionDao.save(submission);
        perform(submission);
        //TODO: Add DB status
        RedisUtils.acknowledge(stringRedisTemplate, streamKey, streamGroupName, record.getId());
        RedisUtils.deleteFromStream(stringRedisTemplate, streamKey, record.getId());
    }

    //TODO: perform should handle all the exceptions
    private void perform(Submission submission) {
        //Create work directory and sandbox
        Map<String, String> directories = null;
        try {
            directories = initializeWorkdir(submission);
        } catch (Exception e) {
            LOGGER.error("Error in creating working directory for isolate", e);
            submission.setStatus(Status.BOXERR);
            return;
        } finally {
            submissionDao.save(submission);
        }

        //Try compiling code
        try {
            boolean compileStatus = compile(submission, directories);
            if (!compileStatus) {
                LOGGER.error("Error in compiling code:{}", submission.getCompileOutput());
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Exception in compiling code", e);
            return;
        } finally {
            submissionDao.save(submission);
        }
        //execute
        try {
            run(submission, directories);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error in run");
        }
        verify(submission, directories);
        submissionDao.save(submission);
        //destroy sandbox
        /*try {
            destroySandbox(submission);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error in destroying sandbox:{}", submission.getId(), e);
        }*/
    }

    private Map<String, String> initializeWorkdir(Submission submission) throws IOException {
        Map<String, String> directories = new HashMap<>();
        String sandboxDirectory = "";
        sandboxDirectory = createSandbox(submission);

        sandboxDirectory = sandboxDirectory.trim();
        String boxDir = sandboxDirectory + "/box";
        String tmpDir = sandboxDirectory + "/tmp";
        String sourceFile = boxDir + "/" + submission.getLanguage().getSourceFile();
        String stdinFile = sandboxDirectory + "/" + STDIN_FILE_NAME;
        String stdoutFile = sandboxDirectory + "/" + STDOUT_FILE_NAME;
        String stderrFile = sandboxDirectory + "/" + STDERR_FILE_NAME;
        String metadataFile = sandboxDirectory + "/" + COMPILE_METADATA_FILE_NAME;

        for (String path : List.of(stdinFile, stdoutFile, stderrFile, metadataFile)) {
            Utils.createFile(path);
        }

        LOGGER.info("Writing source code to {}", sourceFile);
        Files.write(Path.of(sourceFile), submission.getSourceCode().getBytes());
        LOGGER.info("Writing STDIN  to {}", sourceFile);
        if (submission.getStdIn() != null)
            Files.write(Path.of(stdinFile), submission.getStdIn().getBytes());

        directories.put("sandboxDirectory", sandboxDirectory);
        directories.put("boxDir", boxDir);
        directories.put("tmpDir", tmpDir);
        directories.put("sourceFile", sourceFile);
        directories.put("stdinFile", stdinFile);
        directories.put("stdoutFile", stdoutFile);
        directories.put("stderrFile", stderrFile);
        directories.put("metadataFile", metadataFile);

        return directories;

    }

    /**
     * returns true if compilation is successful
     */
    private boolean compile(Submission submission, Map<String, String> directories) throws IOException, InterruptedException {
        String sandboxDirectory = directories.get("sandboxDirectory");
        String boxDir = directories.get("boxDir");
        String metadataFile = directories.get("metadataFile");
        String compilerScript = boxDir + "/compile.sh";
        String compileCommand = submission.getLanguage().getCompileCommand();
        if (!StringUtils.isNotBlank(compileCommand)) {
            LOGGER.info("No compile command found");
            return true;
        }
        File compilerScriptFile = new File(compilerScript);
        Files.write(compilerScriptFile.toPath(), compileCommand.getBytes());
        boolean permissionStatus = compilerScriptFile.setExecutable(true, true);
        if (!permissionStatus) {
            LOGGER.error("Unable to make compiler script executable");
            submission.setStatus(Status.CE);
            return false;
        }

        String compileOutputFile = sandboxDirectory + "/" + COMPILE_OUTPUT_FILE_NAME;
        Utils.createFile(compileOutputFile);

        //TODO: Make parameters adjustable
        String[] parsedCommand = getParsedCompilingCommand(submission);
        ProcessBuilder processBuilder = new ProcessBuilder(parsedCommand);
        processBuilder.directory(new File(sandboxDirectory));
        processBuilder.redirectOutput(new File(compileOutputFile));
        processBuilder.redirectErrorStream(true);
        LOGGER.info("Compiling submission with command:{}", processBuilder.command());
        Process process = processBuilder.start();
        boolean exitedInTime = process.waitFor((long) MAX_WALL_TIME_LIMIT + 5, TimeUnit.SECONDS);
        if (!exitedInTime) {
            process.destroyForcibly();
            LOGGER.error("Compiling process took more than 30 seconds killing!!");
            submission.setStatus(Status.TLE);
            return false;
        }
        int exitValue = process.exitValue();

        String compileOutput = Files.readString(Path.of(compileOutputFile));

        if (StringUtils.isNotBlank(compileOutput))
            submission.setCompileOutput(compileOutput);

        if (exitValue == 0)
            return true;

        Map<String, String> metadata = parseMetadata(Path.of(metadataFile));
        if (metadata.getOrDefault("status", "").equals("TO"))
            submission.setCompileOutput("Compilation Time out");
        submission.setStatus(Status.CE);
        return false;
    }

    private String[] getParsedCompilingCommand(Submission submission) {
        String[] commandArr = {"isolate", "--cg", "-s", "-b", String.valueOf(submission.getId()),
                "--meta", COMPILE_METADATA_FILE_NAME,
                "--stderr-to-stdout", "--stdin", "/dev/null",
                "--time", String.valueOf(submission.getCpuTimeLimit()),
                "--extra-time", String.valueOf(submission.getCpuExtraTimeLimit()),
                "--wall-time", String.valueOf(submission.getWallTimeLimit()),
                "--stack", String.valueOf(submission.getStackLimit()),
                "-p" + submission.getMaxProcessesAndOrThreadsLimit(),
                "--cg-mem=" + submission.getMemoryLimit(),
                "--fsize", String.valueOf(submission.getMaxFileSizeLimit()),
                "-E", "HOME=/tmp",
                "-E", "PATH=\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"",
                "-d", "/etc:noexec",
                "--run",
                "--",
                "/bin/bash", "compile.sh"};

        return commandArr;
    }


    private void run(Submission submission, Map<String, String> directories) throws IOException, InterruptedException {
        String sandboxDir = directories.get("sandboxDirectory");
        String boxDir = directories.get("boxDir");
        String stdIn = directories.get("stdinFile");
        String stdOut = directories.get("stdoutFile");
        String stdErr = directories.get("stderrFile");
        String runScript = boxDir + "/run.sh";
        File runScriptFile = new File(runScript);
        Files.write(runScriptFile.toPath(), submission.getLanguage().getRunCommand().getBytes());
        boolean permissionStatus = runScriptFile.setExecutable(true, true);
        if (!permissionStatus) {
            LOGGER.error("Unable to make run script executable");
            submission.setStatus(Status.BOXERR);
            return;
        }
        String[] runCommand = getParsedRunCommand(submission);
        ProcessBuilder processBuilder = new ProcessBuilder(runCommand);
        processBuilder.directory(new File(sandboxDir));
        processBuilder.redirectInput(new File(stdIn));
        processBuilder.redirectError(new File(stdErr));
        processBuilder.redirectOutput(new File(stdOut));
        LOGGER.info("Running submission with command:{}", processBuilder.command());
        Process process = processBuilder.start();
        boolean exitedInTime = process.waitFor((long) MAX_WALL_TIME_LIMIT + 5, TimeUnit.SECONDS);
        if (!exitedInTime) {
            process.destroyForcibly();
            LOGGER.error("Compiling process took more than {} seconds killing!!", MAX_WALL_TIME_LIMIT + 5);
            submission.setStatus(Status.TLE);
        }
    }

    private String[] getParsedRunCommand(Submission submission) {
        String[] runCommand = {"isolate", "--cg", "-s", "-b", String.valueOf(submission.getId()),
                "--meta", RUN_METADATA_FILE_NAME,
                "--time", String.valueOf(submission.getCpuTimeLimit()),
                "--extra-time", String.valueOf(submission.getCpuExtraTimeLimit()),
                "--wall-time", String.valueOf(submission.getWallTimeLimit()),
                "--stack", String.valueOf(submission.getStackLimit()),
                "-p" + submission.getMaxProcessesAndOrThreadsLimit(),
                "--cg-timing",
                "--cg-mem=" + submission.getMemoryLimit(),
                "--fsize", String.valueOf(submission.getMaxFileSizeLimit()),
                "-E", "HOME=/tmp",
                "-E", "PATH=\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"",
                "-d", "/etc:noexec",
                "--run",
                "--", "/bin/bash", "run.sh"};
        return runCommand;
    }

    private void verify(Submission submission, Map<String, String> directories) {
        String sandboxDir = directories.get("sandboxDirectory");
        Map<String, String> metadata = parseMetadata(Path.of(sandboxDir + "/" + RUN_METADATA_FILE_NAME));
        String stdOut = directories.get("stdoutFile");
        String stdErr = directories.get("stderrFile");
        String programStdout = null;
        try {
            programStdout = Files.readString(Path.of(stdOut));
        } catch (IOException e) {
            LOGGER.error("Error in reading std out file", e);
        }
        submission.setStdOut(programStdout);
        String programStderr = null;
        try {
            programStderr = Files.readString(Path.of(stdErr));
        } catch (IOException e) {
            LOGGER.error("Error in reading std err file", e);
        }
        submission.setStdErr(programStderr);
        submission.setTime(NumberUtils.createFloat(metadata.getOrDefault("time", null)));
        submission.setWallTime(NumberUtils.createFloat(metadata.getOrDefault("time-wall", null)));
        submission.setMemory(NumberUtils.createInteger(metadata.getOrDefault("cg-mem", null)));
        submission.setExitCode(NumberUtils.createInteger(metadata.getOrDefault("exitcode", "0")));
        submission.setExitSignal(NumberUtils.createInteger(metadata.getOrDefault("exitsig", null)));
        submission.setMessage(metadata.getOrDefault("message", null));

        String statusStr = metadata.getOrDefault("status", "");
        submission.setStatus(determineStatus(submission, statusStr));
    }

    private Status determineStatus(Submission submission, String status) {
        if (status.equalsIgnoreCase("TO"))
            return Status.TLE;
        else if (status.equalsIgnoreCase("SG"))
            return Status.findRuntimeErrorByStatusCode(submission.getExitSignal());
        else if (status.equalsIgnoreCase("RE"))
            return Status.NZEC;
        else if (status.equalsIgnoreCase("XX"))
            return Status.BOXERR;
        else if (submission.getExpectedOutput() == null || submission.getExpectedOutput().trim().equalsIgnoreCase(submission.getStdOut().trim()))
            return Status.AC;
        else return Status.WA;
    }

    private Map<String, String> parseMetadata(Path metadataPath) {
        Map<String, String> metadata = new HashMap<>();
        try {

            metadata = Arrays.stream(Files.readString(metadataPath).split("\n"))
                    .collect(Collectors.toMap
                            (
                                    key -> key.split(":")[0],
                                    value -> value.split(":")[1]
                            )
                    );
        } catch (IOException e) {
            LOGGER.error("Metadata file:{} does not exists", metadataPath);
        }

        return metadata;

    }

    private String createSandbox(Submission submission) throws IOException {
        long boxId = submission.getId();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("isolate", "--b", String.valueOf(boxId), "--cg", "--init");
        LOGGER.info("Executing isolate command:{}", processBuilder.command());
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        String output = new String(inputStream.readAllBytes());
        LOGGER.info("output:{}", output);
        return output;
    }

    private void destroySandbox(Submission submission) throws IOException, InterruptedException {
        long boxId = submission.getId();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("isolate", "--b", String.valueOf(boxId), "--cleanup");
        LOGGER.info("Executing isolate command:{}", processBuilder.command());
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        String output = new String(inputStream.readAllBytes());
        boolean exitSuccess = process.waitFor((long) MAX_WALL_TIME_LIMIT + 5, TimeUnit.SECONDS);
        if (!exitSuccess)
            LOGGER.error("Unable to cleanup sandbox: {}", boxId);
        LOGGER.info("cleanup output:{}", output);
    }
}
