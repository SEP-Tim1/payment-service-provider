#!/bin/sh

if [ "$#" -ne 2 ]
then
  echo "You must provide a domain and a directory name"
  exit 1
fi

DOMAIN=$1
DIRECTORY=$2

cd "./${DIRECTORY}"

#generating a private key
winpty openssl genrsa -out $DIRECTORY.key 2048
#generating a csr
winpty openssl req -new -key $DIRECTORY.key -out $DIRECTORY.csr
#generating an .ext file
cat > $DIRECTORY.ext << EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectAltName = @alt_names
[alt_names]
DNS.1 = $DOMAIN
IP.1 = 127.0.0.1
IP.2 = 192.168.43.241
IP.3 = 192.168.0.16
EOF
#generating a certificate
winpty openssl x509 -req -in $DIRECTORY.csr -CA ../CA/myCA.crt -CAkey ../CA/myCA.key -CAcreateserial \
              -out $DIRECTORY.crt -days 365 -sha256 -extfile $DIRECTORY.ext
#generating a keystore
winpty openssl pkcs12 -export -in $DIRECTORY.crt -inkey $DIRECTORY.key \
              -out $DIRECTORY.keystore.p12 -name $DIRECTORY \
              -CAfile ../CA/myCA.crt -password pass:password

cd ../