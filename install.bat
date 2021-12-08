@echo off

cd auth-service
start mvn clean install

cd ../card-payment-service
start mvn clean install

cd ../gateway
start mvn clean install

cd ../payment-request-service
start mvn clean install

cd ../registry
start mvn clean install

cd ../store-service
start mvn clean install