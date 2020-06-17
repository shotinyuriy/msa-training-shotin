docker network create [OPTIONS] NETWORK

docker network create msa-network

docker run -it --network msa-network --rm redis redis-cli -h accounts-redis

winpty docker run -it --network host --rm redis redis-cli -h accounts-redis

winpty docker run -it --network docker_default --rm redis redis-cli -h <container_name>