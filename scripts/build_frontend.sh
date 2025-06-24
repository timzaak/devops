#!/bin/bash

imageName="${dockerRepo}/${dockerImage}:${VERSION}"
echo "build image is: $imageName"

if [ -n "$BuildTest" ]; then
    echo "npm run build:test"
    npm run build:test
else
    echo "npm run build"
    npm run build
fi

docker build -t "$imageName" .
docker push "$imageName"