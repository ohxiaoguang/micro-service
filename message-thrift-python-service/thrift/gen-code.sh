#!/usr/bin/env bash
thrift --gen py -out ../ messageif.thrift

thrift --gen java -out ../../message-thrift-service-api/src/main/java messageif.thrift