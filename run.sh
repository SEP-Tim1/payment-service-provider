#!/bin/bash

docker-compose --env-file ./env/common.env build
docker-compose --env-file ./env/common.env up