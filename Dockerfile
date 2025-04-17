# Use uma imagem do OpenJDK com o Java 21 para o Spring Boot
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo gerado pelo Gradle para a imagem
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Exponha a porta padrão do Spring Boot
EXPOSE 8080

# Comando padrão para rodar o container
ENTRYPOINT ["java", "-jar", "app.jar"]
