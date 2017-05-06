#!/usr/bin/env bash
docker rmi $(docker images -q -f dangling=true)