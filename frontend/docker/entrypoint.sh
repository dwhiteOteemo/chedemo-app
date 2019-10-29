#!/bin/sh
sed -i "s/SERVICE_NAME/$SERVICE_NAME/g" /var/www/localhost/htdocs/config/settings.php
sed -i "s/SERVICE_PORT/$SERVICE_PORT/g" /var/www/localhost/htdocs/config/settings.php
touch /var/log/apache2/access.log
touch /var/log/apache2/error.log
/usr/sbin/httpd -Dforeground & tail -f /var/log/apache2/* & 
tail -f /dev/null
