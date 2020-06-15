# https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html#step-6-stop-docker

git clone https://github.com/confluentinc/cp-all-in-one
cd cp-all-in-one
git checkout 5.5.0-post

cd cp-all-in-one/

docker-compose up -d --build

# Navigate to the Control Center web interface at http://localhost:9021/ and select your cluster.

# create a topic 'pageviews'

# Repeat the previous steps and create a topic named 'users' and click Create with defaults.

# KSQL
docker-compose exec ksql-cli ksql http://ksql-server:8088


docker container ls -aq
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker") && docker system prune -a -f --volumes

# You can rebuild and restart the containers at any time using the
docker-compose up -d --build

# https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html#step-6-stop-docker

# Run the build command just for connect.
docker-compose build --no-cache connect

# Grep Docker Logs
docker-compose logs connect | grep -i Datagen

# docker exec -i -t 6ee6223a3ce3 bash root@6ee6223a3ce3:/#
docker exec -i -t f8d43549a52c bash root@f8d43549a52c:/#



winpty docker exec -i -t f8d43549a52c bash root@f8d43549a52c:/#

winpty docker exec -i -t f8d43549a52c bash root@f8d43549a52c

docker exec -i -t 6ee6223a3ce3 bash
2
root@6ee6223a3ce3:/#
