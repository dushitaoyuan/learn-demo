FROM dushitaoyuan/alpine-oracle-java8:jdk
WORKDIR /springboot
COPY springboot-demo.jar springboot-demo.jar
#日志输出路径
VOLUME /data
EXPOSE 8080
CMD ["nohup","java","-jar","/springboot/springboot-demo.jar","&"]