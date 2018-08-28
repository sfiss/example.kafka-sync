#!/bin/bash

cd kafka_2.11-2.0.0

# Start server 
start "bin\windows\zookeeper-server-start.bat" config/zookeeper.properties
start "bin\windows\kafka-server-start.bat" config/server.properties

# Create topics
start "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-db
start "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-service
start "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-aggr