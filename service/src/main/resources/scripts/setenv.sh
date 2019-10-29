export url=jdbc:mysql://localhost/
export dbName=chedemo
export driver=org.gjt.mm.mysql.Driver
export userName=chedemo
export password=chedemo
export db_descriptor=jdbc/chedemo
export report_cache=false
export charts_cache=false
export redis_server=localhost
export service_port=9090
export server_name=localhost

cd ../../../../
gradle clean fatJar
java -cp build/libs/service-all-1.0.jar org.djw.Main