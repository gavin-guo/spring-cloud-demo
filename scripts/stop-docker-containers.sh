#!/usr/bin/env bash
cd docker-compose
pwd
docker-compose stop
docker-compose rm -f
docker ps