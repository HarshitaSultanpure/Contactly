package com.contact.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entities.Contact;
import com.contact.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	// custom pagination method to get pagination functionality

	@Query("from Contact as c where c.user.id =:userId") // will provide the list of contacts of a user who is already
 															// loggedin.
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pegeable);
	//pageable will have two things: current page and number of contact per page
	//a page is a sublist of a list of objects.
	
	//search
	public List<Contact> findByNameContainingAndUser(String keywords,User user);

}
