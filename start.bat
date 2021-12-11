@echo off
start java -jar zipkin-server-2.23.9-exec.jar
start java -jar registry/target/registry-0.0.1-SNAPSHOT.jar
start java -jar gateway/target/gateway-0.0.1-SNAPSHOT.jar
start java -jar auth-service/target/auth-0.0.1-SNAPSHOT.jar
start java -jar store-service/target/store-0.0.1-SNAPSHOT.jar
start java -jar card-payment-service/target/payment.card-0.0.1-SNAPSHOT.jar
start java -jar card-payment-service/target/payment.card-0.0.1-SNAPSHOT.jar --server.port=8123 --spring.application.name=card-payment-service
start java -jar payment-request-service/target/request-0.0.1-SNAPSHOT.jar
