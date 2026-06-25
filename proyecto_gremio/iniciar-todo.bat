@echo off

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd eureka-server
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > null

echo Iniciando API Gateway...
cd ../gateway
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Avenureros...
cd ../aventureros
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Parties
cd ../parties
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microserivicio Gremios
cd ../gremios
start cmd /k "mvnw spring-boot:run"