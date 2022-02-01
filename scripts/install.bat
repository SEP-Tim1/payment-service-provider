@echo off

cd e-commerce-platform/web-shop-back
start mvn clean install

cd ../../first-bank/first-bank-back
start mvn clean install

cd ../../payment-service-provider

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

cd ../bitcoin-payment-service
start mvn clean install

cd ../paypal-payment-service
start mvn clean install

cd ../qrcode-payment-service
start mvn clean install

cd ../../