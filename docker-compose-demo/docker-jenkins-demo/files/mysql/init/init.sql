-- 授权允许远程访问mysql
grant all privileges on *.* to root@'%' identified by 'root123';
-- 刷新权限
flush privileges;

source /docker-entrypoint-initdb.d/user.sql;