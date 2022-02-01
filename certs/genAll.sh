#!/bin/sh

# sh ./genCA.sh
# echo "CA generated"

# sh ./genCert.sh localhost webshop-front
# echo "webshop front generated"

# sh ./genCert.sh localhost webshop-back
# echo "webshop back generated"

# sh ./genCert.sh localhost psp-front
# echo "psp front generated"

# sh ./genCert.sh localhost gateway
# echo "gateway generated"

# sh ./genCert.sh localhost bank1-front
# echo "bank1 front generated"

# sh ./genCert.sh localhost bank1-back
# echo "bank1-back generated"

# sh ./genCert.sh localhost registry
# echo "registry generated"

# sh ./genCert.sh localhost auth-service
# echo "auth-service generated"

# sh ./genCert.sh localhost store-service
# echo "store-service generated"

# sh ./genCert.sh localhost payment-request-service
# echo "payment-request-service generated"

# sh ./genCert.sh localhost bitcoin-payment-service
# echo "bitcoin-payment-service generated"

# sh ./genCert.sh localhost paypal-payment-service
# echo "paypal-payment-service generated"

# sh ./genCert.sh localhost card-payment-service
# echo "card-payment-service generated"

# sh ./genCert.sh localhost qrcode-payment-service
# echo "qrcode-payment-service generated"

# sh ./genCert.sh localhost bank2-front
# echo "bank2 front generated"

# sh ./genCert.sh localhost bank2-back
# echo "bank2-back generated"

# sh ./genCert.sh localhost pcc
# echo "pcc generated"

# cd ./gateway
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp gateway.keystore.p12 ../../gateway/src/main/resources/gateway.keystore.p12
# cp truststore.p12 ../../gateway/src/main/resources/truststore.p12
# cd ../

# cd ./registry
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp registry.keystore.p12 ../../registry/src/main/resources/registry.keystore.p12
# cp truststore.p12 ../../registry/src/main/resources/truststore.p12
# cd ../

# cd ./auth-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp auth-service.keystore.p12 ../../auth-service/src/main/resources/auth-service.keystore.p12
# cp truststore.p12 ../../auth-service/src/main/resources/truststore.p12
# cd ../

# cd ./store-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp store-service.keystore.p12 ../../store-service/src/main/resources/store-service.keystore.p12
# cp truststore.p12 ../../store-service/src/main/resources/truststore.p12
# cd ../

# cd ./payment-request-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../webshop-back webshop-back
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp payment-request-service.keystore.p12 ../../payment-request-service/src/main/resources/payment-request-service.keystore.p12
# cp truststore.p12 ../../payment-request-service/src/main/resources/truststore.p12
# cd ../

# cd ./card-payment-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank1-back bank1-back
# sh ../genTruststore.sh ../bank2-back bank2-back
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp card-payment-service.keystore.p12 ../../card-payment-service/src/main/resources/card-payment-service.keystore.p12
# cp truststore.p12 ../../card-payment-service/src/main/resources/truststore.p12
# cd ../

# cd ./bitcoin-payment-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp bitcoin-payment-service.keystore.p12 ../../bitcoin-payment-service/src/main/resources/bitcoin-payment-service.keystore.p12
# cp truststore.p12 ../../bitcoin-payment-service/src/main/resources/truststore.p12
# cd ../

# cd ./qrcode-payment-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank1-back bank1-back
# sh ../genTruststore.sh ../bank2-back bank2-back
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# cp qrcode-payment-service.keystore.p12 ../../qrcode-payment-service/src/main/resources/qrcode-payment-service.keystore.p12
# cp truststore.p12 ../../qrcode-payment-service/src/main/resources/truststore.p12
# cd ../

# cd ./paypal-payment-service
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service
# cp paypal-payment-service.keystore.p12 ../../paypal-payment-service/src/main/resources/paypal-payment-service.keystore.p12
# cp truststore.p12 ../../paypal-payment-service/src/main/resources/truststore.p12
# cd ../

# cd ./bank1-back
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank2-back bank2-back
# sh ../genTruststore.sh ../pcc pcc
# sh ../genTruststore.sh ../gateway gateway
# cp bank1-back.keystore.p12 ../../../first-bank/first-bank-back/src/main/resources/bank1-back.keystore.p12
# cp truststore.p12 ../../../first-bank/first-bank-back/src/main/resources/truststore.p12
# cd ../

# cd ./bank2-back
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank1-back bank1-back
# sh ../genTruststore.sh ../pcc pcc
# sh ../genTruststore.sh ../gateway gateway
# cp bank2-back.keystore.p12 ../../../second-bank/second-bank-back/src/main/resources/bank2-back.keystore.p12
# cp truststore.p12 ../../../second-bank/second-bank-back/src/main/resources/truststore.p12
# cd ../

# cd ./pcc
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank1-back bank1-back
# sh ../genTruststore.sh ../bank2-back bank2-back
# cp pcc.keystore.p12 ../../../payment-card-center/src/main/resources/pcc.keystore.p12
# cp truststore.p12 ../../../payment-card-center/src/main/resources/truststore.p12
# cd ../

# cd ./webshop-back
# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../gateway gateway
# cp webshop-back.keystore.p12 ../../../e-commerce-platform/web-shop-back/src/main/resources/webshop-back.keystore.p12
# cp truststore.p12 ../../../e-commerce-platform/web-shop-back/src/main/resources/truststore.p12
# cd ../

cd ./webshop-front
cp webshop-front.key ../../../e-commerce-platform/web-shop-front/ssl/webshop-front.key
cp webshop-front.crt ../../../e-commerce-platform/web-shop-front/ssl/webshop-front.crt
cd ../

cd ./bank1-front
cp bank1-front.key ../../../first-bank/first-bank-front/ssl/bank1-front.key
cp bank1-front.crt ../../../first-bank/first-bank-front/ssl/bank1-front.crt
cd ../

cd ./bank2-front
cp bank2-front.key ../../../second-bank/second-bank-front/ssl/bank2-front.key
cp bank2-front.crt ../../../second-bank/second-bank-front/ssl/bank2-front.crt
cd ../

cd ./psp-front
cp psp-front.key ../../psp-front/ssl/psp-front.key
cp psp-front.crt ../../psp-front/ssl/psp-front.crt
cd ../

# sh ../genTruststore.sh ../CA myCA
# sh ../genTruststore.sh ../bank1 bank1
# sh ../genTruststore.sh ../bank2 bank2
# sh ../genTruststore.sh ../pcc pcc
# sh ../genTruststore.sh ../webshop-back webshop-back
# sh ../genTruststore.sh ../gateway gateway
# sh ../genTruststore.sh ../registry registry
# sh ../genTruststore.sh ../auth-service auth-service
# sh ../genTruststore.sh ../store-service store-service
# sh ../genTruststore.sh ../payment-request-service payment-request-service
# sh ../genTruststore.sh ../card-payment-service card-payment-service
# sh ../genTruststore.sh ../paypal-payment-service paypal-payment-service
# sh ../genTruststore.sh ../bitcoin-payment-service bitcoin-payment-service
# sh ../genTruststore.sh ../qrcode-payment-service qrcode-payment-service