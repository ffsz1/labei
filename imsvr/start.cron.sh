port=$1
cp cron-svr.js cron-svr_$port.js &&
pm2 start cron-svr_$port.js -- $port
