CREATE TABLE IF NOT EXISTS "language" (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	"name" varchar NOT NULL,
	"version" varchar NOT NULL,
	source_file varchar NOT NULL,
	compile_command varchar NULL,
	run_command varchar NOT NULL,
	CONSTRAINT language_pk PRIMARY KEY (id)
);

INSERT INTO public."language"
("name","version",source_file,compile_command,run_command)
VALUES
('JAVA','openjdk 17','Main.java','/usr/local/openjdk17/bin/javac Main.java','/usr/local/openjdk17/bin/java Main'),
('PYTHON','3.8.1','script.py',NULL,'/usr/local/python-3.8.1/bin/python3 script.py');


CREATE TABLE IF NOT EXISTS submission (
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE),
	created_at timestamp NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
	"input" varchar NULL,
	expected_output varchar NULL,
	source_code text NOT NULL,
	language_id int8 NOT NULL,
	"output" varchar NULL,
	status int4 NOT NULL DEFAULT 1,
	compile_output text NULL,
	std_in varchar NULL,
	finished_at timestamp NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
	std_err varchar NULL,
	cpu_time_limit float4 NULL,
	cpu_extra_time_limit float4 NULL,
	wall_time_limit float4 NULL,
	memory_limit float4 NULL,
	stack_limit float4 NULL,
	max_process_and_or_thread_limit int8 NULL,
	max_file_size_limit int8 NULL,
	exit_code int8 NULL,
	exit_signal int8 NULL,
	message varchar NULL,
	wall_time float4 NULL,
	"time" float4 NULL,
	execution_host varchar NULL,
	std_out varchar NULL,
	memory int8 NULL,
	CONSTRAINT submission_pkey PRIMARY KEY (id)
);

ALTER TABLE public.submission ADD CONSTRAINT submission_language_fk FOREIGN KEY (language_id) REFERENCES "language"(id);