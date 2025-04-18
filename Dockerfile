# Etapa 1: Build
FROM gradle:8.4-jdk21 AS build
WORKDIR /app

# Copia os arquivos do projeto para o container (cuidado com o .dockerignore)
COPY . .

# Executa o build com Gradle, gerando o arquivo "app.jar"
RUN gradle bootJar

# Use uma imagem do OpenJDK com o Java 21 para o Spring Boot
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo gerado pelo Gradle para a imagem
ARG JAR_FILE=build/libs/currency-converter.jar
ENV PORT=8080
ENV JAR_FILE_PATH=${JAR_FILE}

COPY ${JAR_FILE} currency-converter.jar

# Exponha a porta padrão do Spring Boot
EXPOSE ${PORT}

# Comando padrão para rodar o container
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "currency-converter.jar"]
