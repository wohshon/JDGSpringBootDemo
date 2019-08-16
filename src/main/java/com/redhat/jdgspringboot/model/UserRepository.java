package com.redhat.jdgspringboot.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserObject, String> {
    List<UserObject> findByName(String name);

}
