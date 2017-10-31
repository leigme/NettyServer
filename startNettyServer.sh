#!/bin/sh
echo "start NettyServer ..."
nohup java -jar /home/Tools/NettyServer.jar >/dev/null 2>&1 &
echo "start NettyServer done!"
