# online code execution system

------------

Online code execution system is a scalable and robust code execution engine.
Which can be plugged in existing system and can act as code judge

### components

1. Server
2. Worker

### Server

Server contains all the controllers for interacting with workers.<br/>
It creates a new entry in database and at the same time in redis streams

------------

### Worker

Worker can be multiple instances that does the heavy lifting of running code.<br/>
Under the hood worker uses isolate library for running code in sandboxed environment.

------------

### Sequence diagram

![sequence diagram](Online%20code%20execution%20system.png)

------------

### How to run

Start with only one replica

> docker compose -f compose-file.yml up --build

To start with 3 replica of worker
> docker compose -f compose-file.yml up --build --scale worker=3

Above command will spin up 4 containers

1. server
2. worker
3. pgDB
4. redis

- Swagger UI is hosted on port http://localhost:8000/
- Redis Insight can be accessed on port http://localhost:8001/
- Postgres database can be connect on port 5432 using any db client

To stop containers
> docker compose -f compose-file.yml down

------------

### Supported languages

1. Java (openjdk 17)
2. Python (3.8.1)


