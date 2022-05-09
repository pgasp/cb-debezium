# cb-datahub


Create a Kafka cluster

Then install the cluster operator and associated resources:
curl -L https://github.com/strimzi/strimzi-kafka-operator/releases/download/0.16.1/strimzi-cluster-operator-0.16.1.yaml \
  | sed 's/namespace: .*/namespace: kafka/' \
  | kubectl apply -f - -n kafka

  And spin up a Kafka cluster, waiting until it’s ready:

kubectl -n kafka \
    apply -f https://raw.githubusercontent.com/strimzi/strimzi-kafka-operator/0.16.1/examples/kafka/kafka-per   sistent-single.yaml \
  && kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka

Setting up Couchbase
Install Couchbase with Docker:

docker run -d  --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase

You’ll also need to setup that Couchbase cluster as normal. I created a bucket called “staging”.

Running Zookeeper, Kafka, MySQL
For the most part, you’ll just need to follow the Debezium tutorial. Make sure to read through all the details, but the short version is to take these steps:

Run a Zookeeper image (this is required for Kafka):

docker run -it --rm --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 debezium/zookeeper:1.8

Run a Kafka image (linked to Zookeeper):

docker run -it --rm --name kafka -p 9092:9092 --link zookeeper:zookeeper debezium/kafka:1.8

Start a MySQL database (Debezium supplies a Docker image that already contains some sample data):

docker run -it --rm --name mysql --security-opt seccomp=unconfined -p 3306:3306 -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw debezium/example-mysql:1.8




Then, in another terminal, we can run the command line client:

docker run -it --rm --name mysqlterm --link mysql --rm mysql:5.7 sh \
  -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" \
  -P"$MYSQL_PORT_3306_TCP_PORT" \
  -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'



  At the mysql> prompt we can switch to the “inventory” database and show the tables in it:

mysql> use inventory;
mysql> show tables;


Preparing Kafka Connectors
At this point in my journey, I must diverge slightly from the Debezium tutorial. That tutorial shows you how to start a Debezium MySQL Connector (with another Docker image and a REST request). However, I want to introduce the Couchbase Kafka Connector.

The easiest way to do this is to create a custom Docker image. This will use the Debezium Kafka connect image as a base, and simply add the Couchbase Kafka Connect JAR file to it. To do this, create a text file called Dockerfile:

FROM debezium/connect:1.8

ADD kafka-connect-couchbase-4.1.4.jar /kafka/connect/couchbase/kafka-connect-couchbase-4.1.4.jar
ADD couchbase-kafkaconnector-debzium-handler-0.0.1-SNAPSHOT.jar /kafka/connect/couchbase/couchbase-kafkaconnector-debzium-handler-0.0.1-SNAPSHOT.jar


Once you’ve done that, build the image: 
docker build . --tag couchbasedebezium. 

I called the image couchbasedebezium, but you can call it whatever you like. After this completes, run docker images, and couchbasedebezium should appear in your local repository:


Starting Kafka Connect
To start Kafka Connect:

docker run -it --rm --name connect -p 8083:8083 -e GROUP_ID=1 -e CONFIG_STORAGE_TOPIC=my_connect_configs -e OFFSET_STORAGE_TOPIC=my_connect_offsets -e STATUS_STORAGE_TOPIC=my_connect_statuses --link zookeeper:zookeeper --link kafka:kafka --link mysql:mysql  couchbasedebezium

Connect to MySQL as a source
Create a POST request to http://localhost:8083/connectors/ with the body:

  curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{ "name": "inventory-connector", "config": { "connector.class": "io.debezium.connector.mysql.MySqlConnector", "tasks.max": "1", "database.hostname": "mysql", "database.port": "3306", "database.user": "debezium", "database.password": "dbz", "database.server.id": "184054", "database.server.name": "dbserver1", "database.include.list": "inventory", "database.history.kafka.bootstrap.servers": "kafka:9092", "database.history.kafka.topic": "dbhistory.inventory" } }'

curl -X DELETE http://localhost:8083/connectors/inventory-connector

Connect to Couchbase as a sink
Create another POST request to http://localhost:8083/connectors/ with the body:

  curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{ "name": "mysql-to-Couchbase-sink", "config": { "connector.class": "com.couchbase.connect.kafka.CouchbaseSinkConnector", "tasks.max": "2", "topics": "dbserver1.inventory.customers,dbserver1.inventory.addresses", "couchbase.seed.nodes": "db", "couchbase.bootstrap.timeout": "2000ms", "couchbase.bucket": "retailsample", "couchbase.username": "Administrator", "couchbase.password": "password", "couchbase.topic.to.collection": "dbserver1.inventory.customers=staging.customers,dbserver1.inventory.addresses=staging.addresses", "couchbase.sink.handler": "com.couchbase.connect.kafka.debezium.DebeziumSinkHandler", "couchbase.document.id": "${/payload/after/id}", "couchbase.durability.persist_to": "NONE", "couchbase.durability.replicate_to": "NONE", "key.converter": "org.apache.kafka.connect.storage.StringConverter", "value.converter": "org.apache.kafka.connect.json.JsonConverter", "value.converter.schemas.enable": "false" } }'

curl -X DELETE http://localhost:8083/connectors/mysql-to-Couchbase-sink


Note  k8s
kubectl config set-context --current --namespace=datahub
kubectl get nodes --show-labels






create scope retailsample.eventing;
create scope retailsample.staging;
create scope retailsample.inventory;
create collection retailsample.eventing.events;
create collection retailsample.inventory.product;
create collection retailsample.inventory.orders;


import data : 
 cbimport json --format list -c http://ec2-52-35-115-100.us-west-2.compute.amazonaws.com:8091 -u Administrator -p password -d 'file://couchmart_1M_formatted_keys.json' -b 'retailsample' --scope-collection-exp "inventory.order" -g %_id% -t 10

 cbimport json --format list -c http://ec2-52-35-115-100.us-west-2.compute.amazonaws.com:8091 -u Administrator -p password -d 'file://couchmart_540K_formatted_keys.json' -b 'retailsample' --scope-collection-exp "inventory.order" -g %_id% -t 10

 cbimport json --format list -c http://ec2-52-35-115-100.us-west-2.compute.amazonaws.com:8091 -u Administrator -p password -d 'file://products.json' -b 'retailsample' --scope-collection-exp "inventory.product" -g %_id% -t 10

  cbimport json --format list -c http://ec2-52-35-115-100.us-west-2.compute.amazonaws.com:8091 -u Administrator -p password -d 'file://couchmart_5OOK_formatted_keys.json' -b 'retailsample' --scope-collection-exp "inventory.order" -g %_id% -t 10