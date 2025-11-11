port=$1
cp proxy.js proxy_$port.js && cp im-svr.js im-svr_$port.js && cp http-svr.js http-svr_$port.js && #cp cron-svr.js cron-svr_$port.js &&
pm2 start  proxy_$port.js  -- $port && pm2 start im-svr_$port.js -- $port && pm2 start http-svr_$port.js -- $port #&& pm2 start cron-svr_$port.js -- $port
