package com.redhat.jdgspringboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.DescriptorParserException;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.redhat.jdgspringboot.model.UserObject;
import com.redhat.jdgspringboot.model.UserObjectMarshaller;
import com.redhat.jdgspringboot.model.UserRepository;

@RestController
@RequestMapping("/rest")
@PropertySource("classpath:application.properties")
public class JDGSpringBootController {
	
	private final RemoteCacheManager cacheManager;
	private UserRepository repository;

	Logger log=Logger.getLogger(this.getClass().getName());


	@Autowired
	public JDGSpringBootController(RemoteCacheManager cacheManager, UserRepository repository) {
	    this.cacheManager = cacheManager;
	    this.repository = repository;
	    log.info("================="+cacheManager.getConfiguration());
    	cacheManager.getCache();
    	SerializationContext serCtx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
    		try {
				serCtx.registerProtoFiles(FileDescriptorSource.fromResources("userobject.proto"));
	    		serCtx.registerMarshaller(new UserObjectMarshaller());
				RemoteCache<String, String> metadataCache = cacheManager.getCache(
					        ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
			    InputStream resource = new ClassPathResource("userobject.proto").getInputStream();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(resource)); 
    	        String proto = reader.lines().collect(Collectors.joining("\n"));
				log.info(proto);
					String schemaFileContents = proto;
					log.info("----"+schemaFileContents);
					metadataCache.put("userobject.proto", schemaFileContents);				
			} catch (DescriptorParserException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	@Autowired
    private Environment env;
	
	/**
	 * Create 100 dummy user records in cache
	 * @return 
	 */
    @RequestMapping("/init")	
    public String index() {
    	log.info("init.....");
		
		 Set<String> names=this.cacheManager.getCacheNames(); Iterator<String>
		 i=names.iterator(); while (i.hasNext()) { log.info("cache: "+i.next()); }
		 
        RemoteCache<String, UserObject> cache=this.cacheManager.getCache("users");
        log.info("initial :"+cache.size());
        ArrayList<UserObject> users=new ArrayList<UserObject>();
		
		  for (int x=0;x < 100;x++) { 
		  UserObject user1=new UserObject();
		  users.add(user1); user1.setName("joe_"+x); 
		  user1.setUserId("id_"+x);
		  cache.put(user1.getUserId(), user1);
		  log.info(user1.getUserId()+":"+((UserObject)cache.get(user1.getUserId())).getName());
		  
		  //save into a db
		  repository.save(user1);
		  
		  }
		         
		  log.info("test query");
	        QueryFactory queryFactory = org.infinispan.client.hotrod.Search.getQueryFactory(cache);
	        Query query = queryFactory.from(UserObject.class).having("name").eq("joe_98").build();
	        List<UserObject> results=query.list();
	        log.info("results "+results.size());
		 return "init";
    }
		
    @GetMapping("/userService/{userId}")
    public String getUser(@PathVariable String userId) {
        RemoteCache<String, UserObject> cache=this.cacheManager.getCache("users");
        UserObject user=cache.get(userId);
        String result=null;
        if (user==null) {
        	log.info("NO FOUND in CACHE, go to DB");
        	Optional<UserObject> results=repository.findById(userId);
        	user=results.get();
        	cache.put(user.getUserId(), user);
        	log.info("Saved in cache");
        } else {
        	log.info("Found in cache");
        	
        }
        Gson gson=new Gson();
        result=gson.toJson(user);
    	return result;
    }

    @PostMapping("/userService/create")
    public String create(@RequestBody UserObject user) {
    	RemoteCache<String, UserObject> cache=this.cacheManager.getCache("users");
    	cache.put(user.getUserId(),user);
    	log.info("saved to cache "+user.getUserId());
        Gson gson=new Gson();
    	log.info("Created User");
    	return gson.toJson(user);
    }

    
    @PostMapping("/userService/update")
    public String update(@RequestBody UserObject user) {
    	RemoteCache<String, UserObject> cache=this.cacheManager.getCache("users");
        log.info("In update user "+user.getUserId());
        cache.put(user.getUserId(),user);
        Gson gson=new Gson();
    	log.info("Updated User");
    	return gson.toJson(user);
    }
    
    @DeleteMapping("/userService/{userId}")
    public String delete(@PathVariable String userId) {
    	RemoteCache<String, UserObject> cache=this.cacheManager.getCache("users");
        log.info("In delete user "+userId);
        UserObject user=cache.remove(userId);
        Gson gson=new Gson();
    	log.info("Removed User");
    	return gson.toJson(user);

    }
   
}
