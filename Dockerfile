# Etapa 1: Build com Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Instala utilitários necessários
RUN apk add --no-cache curl unzip bash

# Copia Maven Wrapper e POM
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copia o código-fonte
COPY src ./src

# Permissão de execução para o Maven Wrapper
RUN chmod +x mvnw

# Build do projeto (skip testes)
RUN ./mvnw clean package -DskipTests
