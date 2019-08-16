# JBoss Datagrid Sample SpringBoot application


## Pre-req

- jboss-datagrid-7.3.0-server
  - setup a new datasource to point to mysql DB, please refer to snippert.xml and application.properties for the DB endpoint
  - create a new infinispan cache deinition as defined in snippert.xml
- mysql/ maria db 
- maven installed and setup

## Startup the jboss datagrid server
Data grid is setup to use jdbc cache store. Upon successful configuration and startup of the server, a `JDG_users` table should be created in the database.

Sample command:

```<SERVER_HOME>/bin/standalone.sh -b 127.0.0.1 -Djboss.socket.binding.port-offset=0 -c clustered.xml -Djboss.node.name=node1 -Djboss.server.base.dir=/home/virtuser/jboss-datagrid-7.3.0-server/node1
```

## startup the spring boot apps

``` 
cd <root folder of app>
./mvnw clean spring-boot:run 
```
Alternatively, there is a binary packaged in the target folder, if all your local configurations are same as the sample app, you can run it directly

`java -jar demo-springboot-0.0.1-SNAPSHOT.jar` 


## Running the demo

### initialising the cache with 100 records

`$ curl localhost:8081/rest/init`

Verify in the console of the spring boot application, and run a query in the database 
`select * from JDG_users`

You should see 100 records returned.


### Find a user by Id

This sample uses a side cache pattern, if the user is in the cache, it will be returned, if not a query to the db will be made, and the result updated to the cache.
In this sample, the user id is id_99
```
$ curl localhost:8081/rest/userService/id_99

{"userId":"id_99","name":"joe_99"}
```

### Create a new User
```
$ curl -X POST -d@new_user.json -H 'Content-type: application/json' localhost:8081/rest/userService/create

{"userId":"new_user","name":"new_user_updated"} 
```

### Update a user
```
$ curl -X POST -d@update_user.json -H 'Content-type: application/json' localhost:8081/rest/userService/update

{"userId":"new_user","name":"new_user_updated"} 
```
### Delete a user
```
$ curl -X DELETE localhost:8081/rest/userService/id_3
```

Verify the outcome of the above operations in both the cache and the db
