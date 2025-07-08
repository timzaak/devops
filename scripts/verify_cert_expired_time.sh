#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <API_SERVER:PORT>"
  echo "Example: $0 127.0.0.1:6443"
  exit 1
fi

TARGET=$1

echo "Check TLS cert expired time: $TARGET"
echo


echo | openssl s_client -connect "$TARGET" 2>/dev/null | openssl x509 -noout -dates