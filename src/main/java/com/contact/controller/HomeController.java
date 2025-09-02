package com.contact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping("/home")
	public String home(Model model) //Model ki help se data template pr bhejte h
	{
		model.addAttribute("title", "Home - Contactly");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) //Model ki help se data template pr bhejte h
	{
		model.addAttribute("title", "About - Contactly");
		return "about";
	}
}