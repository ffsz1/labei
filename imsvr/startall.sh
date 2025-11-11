cpunum=`cat /proc/cpuinfo | grep processor | wc -l`
startport=3006
for((i=0;i<$cpunum;i++));  
do   
echo "start" $(expr $[i*10]  + $startport);  
sh start.sh $(expr $[i*10]  + $startport);
done 
sh start.cron.sh $startport;
