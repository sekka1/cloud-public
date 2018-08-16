Gravitee.io
===============



Docker repository: https://github.com/gravitee-io/gravitee-docker

Docker compose install: https://docs.gravitee.io/apim_installguide_docker.html

http://localhost:8000/
http://localhost:8002/#!/


# mongo
docker run \
--name gravitee-mongo \
--net=host \
-d \
mongo:3.4

# elasticsearch

docker run \
--name gravitee-es \
--net=host \
--env http.host=0.0.0.0 \
--env transport.host=0.0.0.0 \
--env xpack.security.enabled=false \
--env xpack.monitoring.enabled=false \
--env cluster.name=elasticsearch \
--env bootstrap.memory_lock=true \
--env "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
-d \
--ulimit memlock=-1:-1 \
--ulimit nofile=65536:65536 \
docker.elastic.co/elasticsearch/elasticsearch:5.4.3


# Gateway

docker run  \
--name gravitee-gateway \
--net=host \
--env "GRAVITEE_MANAGEMENT_MONGODB_URI=mongodb://localhost:27017/gravitee?serverSelectionTimeoutMS=5000&connectTimeoutMS=5000&socketTimeoutMS=5000" \
--env "gravitee_ratelimit_mongodb_uri=mongodb://localhost:27017/gravitee?serverSelectionTimeoutMS=5000&connectTimeoutMS=5000&socketTimeoutMS=5000" \
--env "gravitee_reporters_elasticsearch_endpoints_0=http://localhost:9200" \
--detach  \
graviteeio/gateway:latest

# management-api

docker run \
--net=host \
--name gravitee-management-api \
--env "GRAVITEE_MANAGEMENT_MONGODB_URI=mongodb://localhost:27017/gravitee?serverSelectionTimeoutMS=5000&connectTimeoutMS=5000&socketTimeoutMS=5000" \
--env GRAVITEEIO_ELASTIC_HOST=localhost \
--env GRAVITEEIO_ELASTIC_PORT=9200 \
--env "gravitee_reporters_elasticsearch_endpoints_0=http://localhost:9200" \
--detach  \
graviteeio/management-api:latest

# management-ui

docker run \
--net=host \
--name gravitee-management-ui \
--env MGMT_API_URL=http://localhost:8083/management/ \
--detach  \
graviteeio/management-ui:latest

# login

http://localhost/#!/
  admin:admin

# create your first api

https://docs.gravitee.io/apim_quickstart_publish.html#create_your_api


sample swagger definition:

https://petstore.swagger.io/#/

https://petstore.swagger.io/v2/swagger.json


# Adminstration

## Change the jwt tokenEndpoint


## Change the default admin password

```
htpasswd -bnBC 10 "" password | tr -d ':\n'
```
