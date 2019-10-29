# CheDemoApp

# Release Notes
**v0.1 Initial release**
- values.yaml defines resources for pods
- Added project information to README file

# Description
Chedemo is a simple application designed as a test deployment for a typical CI/CD pipeline. It has a web front end which displays a table of the population and GDP of each country and a simple filter to select individual countries. The backend service exposes endpoints which the frontend application interacts with and relays data from the database. The application consists of three individual containers which communicate with each other through services. 

# Diagram

```
   frontend                backend
+------------+         +--------------+          database
|     PHP    |         |Restlet 2.3.13|        +-----------+
+------------+         +--------------+        |App Schema |
|Apache httpd| <-----> |  openJDK|11  | <----> +-----------+
+------------+         +--------------+        | MySQL 5.7 |
|Alpine Linux|         | Apache Linux |        +-----------+
+------------+         +--------------+

```

# Database Container
The database container is built from the official ```MySQL 5.7``` container with the zipped world GDP database export. During the container build the export is copied to the following location: 

```/docker-entrypoint-initdb.d/chedemo.sql.gz```

Additionally, the following environment variables are set to create the service account, database, and load the export into the database.

```
MYSQL_USER 
MYSQL_PASSWORD
MYSQL_DATABASE
```
When the container spins up it will begin to populate with the exported data. It will take several seconds to go through the startup and import procedure so you should wait until it has finished loading before accessing the application. The default TCP port ```3306``` should be used to expose the database service.

# Backend Container
The backend container is built on Alpine Linux with the package 
```openjdk11-jre-headless==11.0.4_p4-r1```. The container hosts the jar file with the actual application which will be run from the entrypoint. The application is a simple RESTful service which uses the Restlet framework to serve endpoints for requests. It is configurable in terms of the service port and credentials to access the database. Additional parameters may be experimental and should remain set to their defaults. The following environment variables should be set when launching this container:

```
url=jdbc:mysql://<NAME-OF-DATABASE-SERVICE>/
dbName=chedemo
driver=org.gjt.mm.mysql.Driver
userName=chedemo
password=chedemo
db_descriptor=jdbc/chedemo
report_cache=false
charts_cache=false
redis_server=localhost
service_port=<PORT-TO-RUN-THIS-SERVICE-ON>
server_name=localhost
```

When the container spins up it checks for the above settings and if not present will throw an error indicating their absence and shut down. Once running you can test its availability by navigating to ```http://<BACKEND-SERVER-NAME>:9090/services/test``` which should present a JSON response indicating success.

# Frontend Container
The frontend container is built on Alpine Linux with the Apache httpd server and php. The following are the Alpine packages added:

```
apache2 
php7-apache2 
php7-curl 
php7-json
```
The main pages are simple php pages with bootstrap components to hadle UI and formatting. The php rest calls are handle with curl and the php json library. When launching the container the following environment variables are set to identify the backend service name and port.
```
SERVICE_NAME=<NAME-OF-BACKEND-SERVICE>
SERVICE_PORT=<BACKEND-SERVICE-PORT>
```
The container is set to run on the default http port of 80. Once the container spins up it should be able to connect to the service. This can be tested by navigating to ```http://<FRONTEND-SERVER-NAME>/search.php``` page. If the dropdown list of countries contains values then it has successfully connected to the database. Check the container's logs for any errors if it fails to connect. The default port recommended for the service is 9090, but can be set to any desired port as long as the backend pod has been configured to listen on that port.

# Image registries
```
davwhiteoteemo/chedemo-frontend
davwhiteoteemo/demo-backend
davwhiteoteemo/demo-database
```

# Deploying with Helm
The included ```values.yaml``` file is intended to help deploy this application through Helm with either Tiller or through generated and applied manifests. The actual templates for this application are located in the ```helmCharts/chedemo``` directory. Here is a quick way to get started:

1. Install Helm on your workstation. Check Artifactory for a version compatible with your system.
2. Clone this repo:
```
git clone https://github.com/dwhiteOteemo/chedemo-app.git
```
3. CD into the ```chedemo-app``` directory
4. Open the ```values.yaml``` file and make any modifications you need to.
5. Generate the manifests:
```
helm template --values ./values.yaml --output-dir ./manifests ./helmCharts/chedemo
```
6. If all went well, you should have a folder ```manifests/chedemo/templates``` which contains all your generated manifests for the deployment. CD to that directory.
7. You should see the following files:
```
deployment.yaml
ingress.yaml
service.yaml
```
8. Inspect the generated files to confirm your values were rendered as expected. If satisfied go on to the next step.
9. Be sure you have credentials and privileges to a namespace on the cluster.
10. CD back to the ```chedemo-app``` directory.
11. Deploy the application to your namespace:
```
kubectl apply -f manifests/chedemo/templates --recursive -n <YOUR-NAMESPACE>
```
12. Use ```kubectl get pods -n <YOUR-NAMESPACE> -w``` to confirm pods were created successfully.
13. When all the pods are up connect to the frontend pod to test the deployment:
```
kubectl port-forward <NAME-OF-FRONTEND-POD> 8081:80
```
14. Open a browser and enter the URL 
```
http://localhost:8081
``` 
You should see the landing page for the application. Clicking any of the buttons should take you to the search page where you can get the results for all countries or selected countries on the results page in a table. 

The deployment consists of ```3 services, 1 ingress resource, and 3 deployments``` containing one pod each. If deploying through Tiller simply running ```helm delete --purge <YOUR-INTALL-NAME>``` will remove all components of the deployment, otherwise you can simply delete each object type to remove the deployment.

# The Demo App
This application is intended as a starting point for development teams wishing to use the DevOps Application Deployment pipeline and provides simple examples from which to base your initial efforts. The walkthrough above is a standalone workflow which we encourage interested teams to experiment with and integrate into their projects. 

# Deployment on Che
The included ``chedemo.yaml`` will create a workspace in Che. Deploy it with the following:

```
CHESERVER=<yourcheserverurl>;
MYUSER=<yourusername>;
MYPASS=<yourpassword>;
A_TOKEN=$(curl --data "grant_type=password&client_id=admin-cli&username=$MYUSER&password=$MYPASS" https://$CHESERVER/auth/realms/che/protocol/openid-connect/token | jq '. | .access_token' | tr -d '"');
chectl workspace:start --devfile=chedemo.yaml --access-token=$A_TOKEN
```