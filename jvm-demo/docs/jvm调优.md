# jvm 调优
## 基本概念和jvm参数
- 参考博客
https://www.cnblogs.com/halberts/p/11918326.html

## 服务器排查基本命令
```shell
1. 查看 正在运行的进程
jps
ps aux|grep aux
2. 查看进程占用内存
top -p 1697
3. 导出进程线程栈信息
jstack pid > xxx.log
4. 查看进程中cpu占用率最高的线程
top -H -p pid
pid 为10进制,需转换为16进制

5. 查看 jvm内存状态
jmap -heap [pid]

6. 查看JVM堆中对象详细占用情况
jmap -histo [pid]
7. 导出堆
jmap -dump:format=b,file=文件名 [pid]

```

## jvm分析工具

jps:查看本机的Java中进程信息  
jstack:打印线程的栈信息,制作线程Dump  
jmap:打印内存映射,制作堆Dump  
jstat:性能监控工具  
jhat:内存分析工具  
jconsole:简易的可视化控制台    
jvisualvm:功能强大的控制台     
eclipse Memory Analyzer  
Eclipse 提供的一个用于分析JVM 堆Dump文件的插件。借助这个插件可查看对象的内存占用状况，引用关系，分析内存泄露等