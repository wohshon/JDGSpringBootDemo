package com.redhat.jdgspringboot;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.infinispan.client.hotrod.RemoteCacheManager;
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
import org.springframework.web.client.RestTemplate;

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
        return "test";
    	//return "Hello "+remoteCache.get("hello");
    }
		
    @GetMapping("/userService/{userId}")
    public String getUser(@PathVariable String userId) {
        log.info("User Facade- get info for  "+userId);
        //call backend
        final String uri = env.getProperty("backend.endpoint");
        log.info("uri: "+uri);
        RestTemplate restTemplate = new RestTemplate();
        UserObject user=restTemplate.getForObject(uri+userId, UserObject.class);
    	return user.getName() ;
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
