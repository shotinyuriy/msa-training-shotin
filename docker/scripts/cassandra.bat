# https://hub.docker.com/_/cassandra

docker run --name some-cassandra --network some-network -d cassandra:tag

docker run --name accounts-cassandra -d -p 7000:7000 -p 9042:9042 cassandra:3.11

$ docker run --name some-cassandra -v /my/own/datadir:/var/lib/cassandra -d cassandra:tag