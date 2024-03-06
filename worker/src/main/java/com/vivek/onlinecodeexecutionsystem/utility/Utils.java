package com.vivek.onlinecodeexecutionsystem.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static void createFile(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                LOGGER.warn("File already exists:{}", path);
                if (file.delete())
                    LOGGER.warn("Deleted file as it already exists:{}", path);
                else
                    LOGGER.error("Unable to delete file:{}", path);
            }

            if (file.createNewFile())
                LOGGER.info("File created successfully:{}", file.getAbsolutePath());
            else
                LOGGER.warn("File already exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
