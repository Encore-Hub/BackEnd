#base-image
FROM openjdk:17
# 2. Set the working directory
WORKDIR /app

# 3. Copy the jar file into the container
COPY build/libs/*.jar app.jar

# 4. Expose the port the app runs on
EXPOSE 8080

# 5. Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]