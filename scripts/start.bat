@echo off

cd e-commerce-platform/web-shop-back/target
start java -jar -DPSP_URL="https://192.168.0.16:8090" -DFRONT_BASE_URL="https://192.168.0.16:4200" -DWEB_SHOP_HOST="192.168.0.16" web-shop-back-0.0.1-SNAPSHOT.jar
cd ../../web-shop-front
start ng serve --host 0.0.0.0 --configuration production

cd ../../payment-service-provider
start java -jar zipkin-server-2.23.9-exec.jar
start java -jar registry/target/registry-0.0.1-SNAPSHOT.jar
start java -jar gateway/target/gateway-0.0.1-SNAPSHOT.jar
start java -jar auth-service/target/auth-0.0.1-SNAPSHOT.jar
start java -jar store-service/target/store-0.0.1-SNAPSHOT.jar
start java -jar bitcoin-payment-service/target/payment.bitcoin-0.0.1-SNAPSHOT.jar
start java -jar -DPSP_FRONT_HOST="192.168.0.16" paypal-payment-service/target/payment.paypal-0.0.1-SNAPSHOT.jar
start java -jar qrcode-payment-service/target/payment.qrcode-0.0.1-SNAPSHOT.jar
start java -jar card-payment-service/target/payment.card-0.0.1-SNAPSHOT.jar
start java -jar card-payment-service/target/payment.card-0.0.1-SNAPSHOT.jar --server.port=8123 --spring.application.name=card-payment-service
start java -jar payment-request-service/target/request-0.0.1-SNAPSHOT.jar
cd psp-front
start ng serve --host 0.0.0.0 --configuration production

cd ../../first-bank/first-bank-back/target
start java -jar -DBANK1_FRONT_URL="192.168.0.16:4300/" first-bank-0.0.1-SNAPSHOT.jar
cd ../../first-bank-front
start ng serve --host 0.0.0.0 --configuration production
