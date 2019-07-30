package com.redhat.jdgspringboot;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.redhat.jdgspringboot.model.UserObject;

@RestController
@RequestMapping("/rest")
@PropertySource("classpath:application.properties")
public class UserServiceFacadeController {
	Logger log=Logger.getLogger(this.getClass().getName());
    @Autowired
    private Environment env;
    @RequestMapping("/")	
    public String index() {
        return "This is Facade Service";
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
