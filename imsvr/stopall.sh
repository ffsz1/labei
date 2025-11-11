cpunum=`cat /proc/cpuinfo | grep processor | wc -l`
startport=3006
for((i=0;i<$cpunum;i++));  
do   
echo "start" $(expr $[i*10]  + $startport);  
sh stop.sh $(expr $[i*10]  + $startport);
done 
sh stop.cron.sh $startport;
