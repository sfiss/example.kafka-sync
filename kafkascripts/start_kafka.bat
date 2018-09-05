cd ..\kafka_2.11-2.0.0

:: Start server 
start call "bin\windows\zookeeper-server-start.bat" config/zookeeper.properties
start call "bin\windows\kafka-server-start.bat" config/server.properties

pause

:: Create topics
rem call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-d
rem call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-s
rem call "bin\windows\kafka-topics.bat" --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-a

rem pause
:: Log consumer
rem call "bin\windows\kafka-console-consumer.bat" -bootstrap-server localhost:9092 --from-beginning --topic data-s

rem pause