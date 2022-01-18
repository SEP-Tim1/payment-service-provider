#!/bin/sh

cd ./CA
winpty openssl genrsa -out myCA.key 2048
winpty openssl req -x509 -new -nodes -key myCA.key -sha256 -days 3625 -out myCA.crt
cd ../