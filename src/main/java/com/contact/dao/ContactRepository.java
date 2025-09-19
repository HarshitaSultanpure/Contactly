package com.contact.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	// custom pagination method to get pagination functionality

	@Query("from Contact as c where c.user.id =:userId") // will provide the list of contacts of a user who is already
															// loggedin.
	public List<Contact> findContactsByUser(@Param("userId") int userId);
}
