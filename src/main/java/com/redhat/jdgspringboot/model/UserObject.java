package com.redhat.jdgspringboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter @Setter @NoArgsConstructor
@Entity
public class UserObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8930855645313163752L;
	@Id
	private String userId;
	private String name;
	
}
