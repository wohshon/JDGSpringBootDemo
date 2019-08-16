package com.redhat.jdgspringboot;

import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.marshall.jboss.GenericJBossMarshaller;
import org.infinispan.spring.starter.remote.InfinispanRemoteConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.redhat.jdgspringboot.model.UserRepository;

@SpringBootApplication
public class JDGSpringBootApplication {
	@Autowired
    private Environment env;
	@Autowired
	private UserRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(JDGSpringBootApplication.class, args);
		//init caches, create remotely
		
		
	}
	@Bean
	public InfinispanRemoteConfigurer infinispanRemoteConfigurer() {
	    return () -> new ConfigurationBuilder()
	    		
		    	.addServer()
		    	.host(env.getProperty("infinispan.host"))
		    	.port(Integer.parseInt(env.getProperty("infinispan.port")))
		    	.tcpKeepAlive(true)
				.marshaller(GenericJBossMarshaller.class)
				
				  .build();

	}	
	/*
	 * @Bean public LocalEntityManagerFactoryBean entityManagerFactory(){
	 * LocalEntityManagerFactoryBean factoryBean = new
	 * LocalEntityManagerFactoryBean(); factoryBean.setPersistenceUnitName("users");
	 * return factoryBean; }
	 */	/*
	 * @Bean public InfinispanRemoteConfigurer infinispanRemoteConfigurer() { return
	 * () -> new ConfigurationBuilder() .addServer()
	 * .host(env.getProperty("infinispan.host"))
	 * .port(Integer.parseInt(env.getProperty("infinispan.port")))
	 * .tcpKeepAlive(true) .security() .authentication() .enable()
	 * .username("jdgadmin") .password("jdgpassword") .realm("ApplicationRealm")
	 * .serverName("datagrid-service") .saslMechanism("DIGEST-MD5")
	 * .saslQop(SaslQop.AUTH) .ssl() .enable()
	 * .sniHostName(env.getProperty("infinispan.host"))
	 * .trustStoreFileName(env.getProperty("truststore"))
	 * .trustStorePassword("password".toCharArray())
	 * .marshaller(GenericJBossMarshaller.class)
	 * //.marshaller(AdvancedExternalizer.class) .build();
	 * 
	 * }
	 */
	

}
