# Connect to Zookeeper locally. 
```
zkCli.sh -server 127.0.0.1:2193
```
# Create the configuration in Zookeeper: 

```
create /config
create /config/zkClient1
create /config/zkClient1/cfgInZk
set /config/zkClient1/cfgInZk valInThisApp
```

# Configration in Zookeeper shared between all apps 
```
create /config/application
create /config/application/cfgInZkForAllApps
set /config/application/cfgInZkForAllApps valueForCfgInZkForAllApps
```

# Start the application and query configuration: 
```
curl localhost:8080/hello/cfgInZk 
=> valInThisApp
curl localhost:8080/hello/cfgInZkForAllApps 
=> valueForCfgInZkForAllApps%       
```

# Zookeeper in cluster mode

## Copy the zookeeper installation in multiple directories. 

e.g. 

```
/Users/coderprabhu/installed/apache-zookeeper-3.8.0_1
/Users/coderprabhu/installed/apache-zookeeper-3.8.0_2
/Users/coderprabhu/installed/apache-zookeeper-3.8.0_3
```

## Setup zoo.conf for each server 

In the conf folder of each of these instances, copy the configuration files 
from clustersetup/conf folder at root of this repository 

Notice slight change in values for following fields 

```
apache-zookeeper-3.8.0_1: 
dataDir=/Users/coderprabhu/installedappdata/zookeeper1
admin.serverPort=8091

apache-zookeeper-3.8.0_2
dataDir=/Users/coderprabhu/installedappdata/zookeeper2
admin.serverPort=8092

apache-zookeeper-3.8.0_3
dataDir=/Users/coderprabhu/installedappdata/zookeeper3
admin.serverPort=8093
```

Following entry remains same for all servers. 
This is how servers know about each other

```
server.1=localhost:2888:3888;2191
server.2=localhost:2889:3889;2192
server.3=localhost:2890:3890;2193
```

## Self identification for servers 


In the corresponding dataDir config, we need to tell each 
server, what is its name. Create myid file as seen in 
clustersetup/data folder 

```
zookeeper1: 
myid: contents: 
1

zookeeper2: 
myid: contents: 
2

zookeeper3: 
myid: contents: 
3
```

## Start each server: 

navigate to each server and start

```
cd /Users/coderprabhu/installed/apache-zookeeper-3.8.0_1 
bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /Users/coderprabhu/installed/apache-zookeeper-3.8.0_1/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED


cd /Users/coderprabhu/installed/apache-zookeeper-3.8.0_2 
bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /Users/coderprabhu/installed/apache-zookeeper-3.8.0_2/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED

cd /Users/coderprabhu/installed/apache-zookeeper-3.8.0_3
bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /Users/coderprabhu/installed/apache-zookeeper-3.8.0_3/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED

```
## Monitor zookeeper logs

```
cs /Users/coderprabhu/installed/apache-zookeeper-3.8.0_1/logs
tail -f  zookeeper-coderprabhu-server-PraBhuMBP.local.out

cd /Users/coderprabhu/installed/apache-zookeeper-3.8.0_2/logs
tail -f  zookeeper-coderprabhu-server-PraBhuMBP.local.out

cd /Users/coderprabhu/installed/apache-zookeeper-3.8.0_3/logs
tail -f  zookeeper-coderprabhu-server-PraBhuMBP.local.out
```

## Start the client application

```
cd /Users/coderprabhu/git/zookeeper-client-spring-config
./gradlew bootRun

```

## Randomly shutdown and start zookeeper instances 

```
bin/zkServer.sh stop
bin/zkServer.sh start 

```

## Experiment and notice following aspects 

### 1. How the leader election happens in zookeeper logs 

### 2. How Spring client application reconnects to different zookeeper

### 3. How minimum 2 zookeeper instances need to be up for zookeeper cluster to be considered available from spring client's perspective 

### 4. How spring client app keeps trying to reconnect if cluster state is not yet available 

### 5. How value changes when a key is updated in zookeeper 

```
set /config/application/cfgInZkForAllApps valueForCfgInZkForAllApps2

2022-06-14 22:48:23.615  INFO 84656 --- [tor-TreeCache-0] o.s.c.e.event.RefreshEventListener       : Refresh keys changed: [cfgInZkForAllApps]

curl localhost:8080/hello/cfgInZkForAllApps 
valueForCfgInZkForAllApps2%  

```