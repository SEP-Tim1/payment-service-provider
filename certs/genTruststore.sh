#!/bin/sh

if [ "$#" -ne 2 ]
then
  echo "You must provide a cert path, name and truststore name"
  exit 1
fi

CERTPATH=$1
ALIAS=$2

keytool -importcert -file $CERTPATH/$ALIAS.crt -alias $ALIAS \
               -keystore truststore.p12 -storetype PKCS12 \
               -storepass password 