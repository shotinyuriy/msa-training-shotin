#!/bin/sh
echo LISTING DIRECTORY
ls /opt/tarantool
echo STARTING TARANTOOL
tarantoolctl
exec tarantool