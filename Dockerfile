# Use a base image with Java and Maven installed
FROM maven:3.9.5-sapmachine-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Copy the source code to the container
COPY server ./server
COPY worker ./worker

# Build the application using Maven
RUN mvn clean package -DskipTests

# Use a base image with JRE installed
FROM compilers:latest AS runtime

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built in the previous stage to the container
COPY --from=build /app/worker/target/*.jar worker.jar

#Don't Expose the port your application runs on
#EXPOSE 8080
# Command to run the application
CMD ["java", "-jar", "worker.jar"]
