pushd ./cp-all-in-one/cp-all-in-one
docker-compose up -d --build
popd

docker start accounts-cassandra
