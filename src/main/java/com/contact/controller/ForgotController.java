package com.contact.controller;

import java.util.Random;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.contact.dao.UserRepository;
import com.contact.entities.User;
import com.contact.service.EmailService;

@Controller
public class ForgotController {
	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bcrypt;
	
	//email id form handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form"; 
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session)
	{
		//generating OTP of 4 digits
		int otp = random.nextInt(99999);
		
		//send OTP to email
		String subject = "OTP from CID";
		String message =""
				+"<div style='border:1px solid #e2e2e2; padding:20px'>"
				+"<h1>"
				+"OTP is "
				+"<b>"+otp
				+"</b>"
				+"</h1>"
				+"</div>";
		String to = email;
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag)
		{
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else
		{
			session.setAttribute("message", "incorrect email id");
			return "forgot_email_form";
		}
	}
	 
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session)
	{
		int myOtp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		
		if(myOtp == otp)
		{
			//password change form
			 
			User user = this.userRepository.getUserByUserName(email);
			
			if(user == null) 
			{
				session.setAttribute("message", "user does not exist with this email!!");
				return "forgot_email_form";
			}
			else
			{
				
			}
			return "password_change_form";
		}
		else
		{
			session.setAttribute("message", "You entered the wrong OTP");
			return "verify_otp";
		}
	}
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session)
	{
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bcrypt.encode(newpassword));
		this.userRepository.save(user);
		
		
		return "redirect:/signin?change=password changed successfully...";
		
	}
}
