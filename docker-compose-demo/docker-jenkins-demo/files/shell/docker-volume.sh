#!/bin/bash
# docker volume脚本
#创建 docker volume
function create_volume() {
# nginx
mkdir -p /home/nginx/
# mysql
mkdir -p /home/mysql/data /home/mysql/conf/ /home/mysql/init
# 应用程序
mkdir -p /home/app/

}
function clear_volume() {
# nginx
rm -rf -p /home/nginx/
# mysql
rm -rf  /home/mysql/
# 应用程序
rm -rf /home/app/
}





case $1 in
    "create" ) create_volume;;
    "clear" ) clear_volume;;
    * ) echo "$0 create"
        echo "$0 clear"
esac
exit 0