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