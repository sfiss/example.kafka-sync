cd kafka_2.11-2.0.0

:: Start server 
start call "bin\windows\zookeeper-server-start.bat" config/zookeeper.properties
start call "bin\windows\kafka-server-start.bat" config/server.properties

:: Create topics
call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-db
call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-service
call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-aggr

pause