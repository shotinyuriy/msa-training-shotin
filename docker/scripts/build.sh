docker build --build-arg BASE_IMAGE=centos:centos7.2.1511 \
 --build-arg TNT_VER=2.4 \
 --file dockerfiles/centos_7_2.x -t tarantool/vshard:0.1.14  .

docker run -rm -it --name tarantool-centos tarantool/tarantool:2.x-centos7

docker run --rm -it --name tarantool-vshard-0 tarantool/vshard:0.1.14

winpty docker run --rm -it --name tarantool-vshard-0 tarantool/vshard:0.1.14

docker run --rm -it --name tarantool-vshard-0 tarantool/vshard


