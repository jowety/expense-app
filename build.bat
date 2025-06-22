call mvn clean install -DskipTests
docker buildx build -t expense-app:1 .