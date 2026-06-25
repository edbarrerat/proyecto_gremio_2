@echo off

cd gateway
start cmd /k "mvnw spring-boot:run"

cd ../aventureros
start cmd /k "mvnw spring-boot:run"

cd ../parties
start cmd /k "mvnw spring-boot:run"

cd ../gremios
start cmd /k "mvnw spring-boot:run"