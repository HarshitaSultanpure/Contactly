package com.contact.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contact.dao.UserRepository;
import com.contact.entities.User;

@Controller
@RequestMapping("/user")
public class UserController { 

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/index")   //jab /user/index likhenge tb ye wala handler chlega and user_dashboard wali file dikhegi
	public String dashboard(Model model,Principal principal) //through Principal interface we will be able to fetch user name 
	{
		String userName = principal.getName();
		System.out.println(userName);
		
		//get user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println(user);
		
		model.addAttribute("user",user);
		
		return "normal/user_dashboard";
	}
 }
