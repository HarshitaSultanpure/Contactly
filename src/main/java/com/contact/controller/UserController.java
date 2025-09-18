package com.contact.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.UserRepository;
import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController { 

	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute //method for adding common data to response. this method will be executed for all the handlers 
	public void addCommonData(Model model, Principal principal)
	{
		// Gets the currently loggedin user's username (email)
		String userName = principal.getName();
		System.out.println(userName);
		
		//get user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println(user);
		
		model.addAttribute("user",user);
		
		
	}
	
	
	//dashboard home
	@RequestMapping("/index")   //jab /user/index likhenge tb ye wala handler chlega and user_dashboard wali file dikhegi
	public String dashboard(Model model,Principal principal) //through Principal interface we will be able to fetch user name 
	{
		model.addAttribute("title","user dashboard");
		return "normal/user_dashboard";
	}
	
	//open add form handler.......
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact") //will specify that we POST method is used....
	public String processContact(
	@ModelAttribute Contact contact,  //all the data of contact will be stored
	@RequestParam("profileImage") MultipartFile file, //image file will be stored
	Principal principal, HttpSession session) //will return view
	{
	try {	
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		  
		//processing and uploading file
		if(file.isEmpty())
		{
			System.out.println("file empty");
		}
		else
		{
			contact.setImage(file.getOriginalFilename());
			File saveFile = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			//try-with-resources to ensure the InputStream is closed
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
			System.out.println("img uploaded");
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		
		System.out.println(contact);
		System.out.println("added to db");
		
		//success message 
		session.setAttribute("message", new Message("contact is added successfully", "success"));
	}
	catch(Exception e)
	{
		System.out.println("ERROR: "+e.getMessage());
		e.printStackTrace();
		
		//data not added error message
		session.setAttribute("message", new Message("something went wrong", "danger"));

	}

	return "normal/add_contact_form";
	}
	
 }
