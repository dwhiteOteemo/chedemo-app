FROM alpine
RUN apk update
RUN apk add unzip
RUN apk add apache2 php7-apache2 php7-curl php7-json

ENV server_name=chedemo-backend
ENV service_port=9090

RUN tail -f /dev/null &
