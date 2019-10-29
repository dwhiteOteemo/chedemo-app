FROM alpine
RUN apk update
RUN apk add unzip
RUN apk add apache2 php7-apache2 php7-curl php7-json

ENV SERVICE_NAME=chedemo-backend
ENV SERVICE_PORT=9090

COPY html.tgz /tmp
RUN set -ex; \
	cd /tmp; \
	tar xzf html.tgz; \
    cd /var/www/localhost/htdocs; \
	rm index.html; \
	mv /tmp/html/* .
COPY entrypoint.sh /entrypoint.sh
RUN tail -f /dev/null &
RUN chmod +x /entrypoint.sh
EXPOSE 80
ENTRYPOINT ["/entrypoint.sh"]
