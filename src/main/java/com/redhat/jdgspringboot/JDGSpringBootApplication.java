package com.redhat.jdgspringboot;

import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.SaslQop;
import org.infinispan.spring.starter.remote.InfinispanRemoteConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JDGSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JDGSpringBootApplication.class, args);
		//init caches, create remotely
	}
	
	@Bean
	public InfinispanRemoteConfigurer infinispanRemoteConfigurer() {
	    return () -> new ConfigurationBuilder()
		    	.addServer()
		    	//.host("datagrid-service-hotrod-ext-route-jdg.apps.cluster-sgp-ae3e.sgp-ae3e.openshiftworkshop.com")
		    	//.port(80)
		    	.host("datagrid-service.jdg.svc.cluster.local")
		    	.port(11222)
	    		.security().authentication()
	    		  .enable()
	    		  .username("jdgadmin")
	    		  .password("jdgpassword")
	    		  .realm("ApplicationRealm")
	    		  .serverName("datagrid-service")
	    		  .saslMechanism("DIGEST-MD5")
	    		  .saslQop(SaslQop.AUTH)
	    		  .build();
/*	    	.addServer()
	    	.host("datagrid-service-hotrod-ext-route-jdg.apps.cluster-sgp-ae3e.sgp-ae3e.openshiftworkshop.com")
	    	.port(443)
	    	.security()
	    	.ssl()
	    	.enable()
	    	.sniHostName("datagrid-service.jdg.svc.cluster.local")
	    	.trustStoreFileName("truststore.jks")
	    	.keyAlias("jdg")
	    	.trustStorePassword("password".toCharArray())
	        .build();*/
	}	

	

}
