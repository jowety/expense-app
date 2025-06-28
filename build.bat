REM Build the Spring Boot application (skipping tests)
call mvn clean install -DskipTests

REM Stop the existing container if it's running (ignore errors if it's not running)
docker stop expense-app

REM Remove the existing container if it exists (ignore errors if it doesn't exist)
docker rm expense-app

REM *** KEY FIX: Remove the old image with the same tag before building the new one ***
REM This ensures the new build truly replaces the tag.
docker rmi expense-app:1

REM Build the new Docker image and tag it
docker buildx build -t expense-app:1 .

REM Run the new Docker container from the freshly built image
docker run -d -p 8080:8080 --name expense-app expense-app:1