package com.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.UserRepository;
import com.contact.entities.User;
import com.contact.helper.Message;



@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	//home handler.......................
	@RequestMapping("/")
	public String home(Model model) //Model ki help se data template pr bhejte h
	{
		model.addAttribute("title", "Home - Contactly");
		return "home";
	}
	
	//about handler......................
	@RequestMapping("/about")
	public String about(Model model) //Model ki help se data template pr bhejte h
	{
		model.addAttribute("title", "About - Contactly");
		return "about";
	}
	
	//signup handler.....................
	@RequestMapping("/signup")
	public String signup(Model model) //Model ki help se data template pr bhejte h
	{
		model.addAttribute("title", "Register - Contactly");
		model.addAttribute("user", new User()); //user name ki key me data store ho jaega form ka
		return "signup";
	}
	
	//handler for registering user   
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)  //with @Valid all the rules will be applied
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1, @RequestParam(value = "agreement", defaultValue="false") boolean agreement, // Reads 'agreement' checkbox value
			Model model, // Used to pass data to the view
			HttpSession session ) 
	{
		try
		{
			//Check for form validation errors
			if(result1.hasErrors())  //agr koi error hogi to vo signup page pr hi show ho jaegi 
			{
				System.out.println("ERROR "+result1.toString());
				model.addAttribute("user", user);
				return "signup"; 
			}
			
			//Check for the agreement checkbox
			if(!agreement)
			{
				System.out.println("didnt agree conditions");
				//throw new Exception("You have not agreed to the terms and conditions");
				model.addAttribute("user", user); // Retain form data
				model.addAttribute("message", new Message("You have not agreed to the terms and conditions", "alert-danger"));
				
				return "signup";
			}
			
			//Save the user if there are no errors
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement"+ agreement); 
			System.out.println("USER"+ user);
			
			User result = this.userRepository.save(user);  
			
			model.addAttribute("user", new User());
			//successfully registered....
			//session.setAttribute("message", new Message("successfully registered", "alert-success"));
			return "login"; 
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong!!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","login page");
		return "login";
	}
}