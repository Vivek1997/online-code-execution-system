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

### Supported languages

1. Java (openjdk 17)
2. Python (3.8.1)


