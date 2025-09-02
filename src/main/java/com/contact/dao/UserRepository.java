package com.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contact.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
