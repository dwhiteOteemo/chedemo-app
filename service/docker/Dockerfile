FROM quay.io/eclipse/che-java11-gradle:nightly
COPY service-all-1.0.jar /home/user/
ENV url=jdbc:mysql://chedemo-db/
ENV dbName=chedemo
ENV driver=org.gjt.mm.mysql.Driver
ENV userName=chedemo
ENV password=chedemo
ENV db_descriptor=jdbc/chedemo
ENV report_cache=false
ENV charts_cache=false
ENV redis_server=localhost
ENV service_port=9090
ENV server_name=localhost

EXPOSE 9090

WORKDIR /home/user
