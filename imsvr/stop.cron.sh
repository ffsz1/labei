port=$1
cp cron-svr.js cron-svr_$port.js && pm2 delete cron-svr_$port
