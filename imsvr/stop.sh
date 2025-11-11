port=$1
cp proxy.js proxy_$port.js && cp im-svr.js im-svr_$port.js && cp http-svr.js http-svr_$port.js && #cp cron-svr.js cron-svr_$port.js &&
pm2 delete  proxy_$port && pm2 delete im-svr_$port && pm2 delete http-svr_$port #&& pm2 delete cron-svr_$port
