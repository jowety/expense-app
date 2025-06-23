call mvn clean install -DskipTests
docker buildx build -t expense-app:1 .
docker stop expense-app
docker rm expense-app
docker run -d -p 8080:8080 --name expense-app expense-app:1