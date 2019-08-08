package com.redhat.jdgspringboot;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redhat.jdgspringboot.model.UserObject;

@RestController
@RequestMapping("/rest")
@PropertySource("classpath:application.properties")
public class JDGSpringBootController {
	
	private final RemoteCacheManager cacheManager;

	Logger log=Logger.getLogger(this.getClass().getName());


	@Autowired
	public JDGSpringBootController(RemoteCacheManager cacheManager) {
	    this.cacheManager = cacheManager;
	    log.info("================="+cacheManager.getConfiguration());
    	cacheManager.getCache();

	}
	@Autowired
    private Environment env;
    @RequestMapping("/")	
    public String index() {
    	//test jdg
    	log.info("init.....");
    	log.info("================="+cacheManager.getConfiguration());
		
		 Set<String> names=this.cacheManager.getCacheNames(); Iterator<String>
		 i=names.iterator(); while (i.hasNext()) { log.info("cache: "+i.next()); }
		 
    	//RemoteCache<String, String> remoteCache=this.cacheManager.getCache("default");
    	//log.info("remoteCache:"+remoteCache);
        RemoteCache<String, Object> cache=this.cacheManager.getCache("default");
        cache.put("key1", "1234");
        log.info("key 1:"+cache.get("key1"));
        for (int x=0;x < 100;x++) {
            UserObject user1=new UserObject();
            user1.setName("joe"+x);
            user1.setUserId("0000123"+x);
            cache.put("key"+x, user1);
            log.info("key "+x+":"+((UserObject)cache.get("key"+x)).getName());
        	
        }
		/*
		 * String serverHost = "";// The address of your JDG server int serverJmxPort =
		 * 0; // The JMX port of your server String cacheContainerName = ""; // The name
		 * of your cache container String schemaFileName = ""; // The name of the schema
		 * file String schemaFileContents = ""; // The Protobuf schema file contents
		 * 
		 * JMXConnector jmxConnector; try { jmxConnector =
		 * JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remoting-jmx://" +
		 * serverHost + ":" + serverJmxPort)); MBeanServerConnection jmxConnection =
		 * jmxConnector.getMBeanServerConnection();
		 * 
		 * ObjectName protobufMetadataManagerObjName = new
		 * ObjectName("jboss.infinispan:type=RemoteQuery,name=" +
		 * ObjectName.quote(cacheContainerName) + ",component=ProtobufMetadataManager");
		 * 
		 * jmxConnection.invoke(protobufMetadataManagerObjName,"registerProtofile", new
		 * Object[]{schemaFileName, schemaFileContents}, new
		 * String[]{String.class.getName(), String.class.getName()});
		 * jmxConnector.close(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		 return "test";
    	//return "Hello "+remoteCache.get("hello");
    }
		
    @GetMapping("/userService/{userId}")
    public String getUser(@PathVariable String userId) {
        RemoteCache<String, Object> cache=this.cacheManager.getCache("default");
        //(UserObject)cache.get("key"+x)).getName()
        //QueryFactory queryFactory = Search.getQueryFactory(cache);
        //Query query = queryFactory.from(UserObject.class).having("userId").eq(userId).build();
        
        //List<UserObject> results=query.list();
        //log.info("results "+results.size());
        String name=(String)cache.get(userId);
    	return name ;
        //return "This is User Service  : " ;
    }

    @PostMapping("/userService/create")
    public String create(@RequestBody UserObject user) {
        log.info("In create user "+user.getUserId());
    	return "saved" ;
    }

    @PostMapping("/userService/update")
    public String update(@RequestBody UserObject user) {
        log.info("In create user "+user.getUserId());
    	return "updated" ;
    }
    
    @DeleteMapping("/userService/delete/{userId}")
    public String delete(@PathVariable String userId) {
        log.info("In create user "+userId);
    	return "deleted" ;
    }
   
}
