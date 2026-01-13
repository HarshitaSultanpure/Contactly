package com.contact.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.helper.Message;

@Controller
//@RequestMapping specifies the URL path for the controller or method.
//Here, "/user" is a relative URL, meaning it is relative to the application's base URL 
//(http://localhost:8080) rather than the full absolute URL (http://localhost:8080/user).
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute // method for adding common data to response. this method will be executed for
					// all the handlers
	public void addCommonData(Model model, Principal principal) {
		// Gets the currently loggedin user's username (email)
		String userName = principal.getName();
		System.out.println(userName);

		// get user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println(user);

		model.addAttribute("user", user);

	}

	// dashboard home
	@RequestMapping("/index") // jab /user/index likhenge tb ye wala handler chlega and user_dashboard wali
								// file dikhegi
	public String dashboard(Model model, Principal principal) // through Principal interface we will be able to fetch user name
	{
		model.addAttribute("title", "user dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler.......
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping("/process-contact") // will specify that we POST method is used....
	public String processContact(@ModelAttribute Contact contact, // all the data of contact will be stored
			@RequestParam("profileImage") MultipartFile file, // image file will be stored
			Principal principal, HttpSession session) // will return view
	{
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {
				System.out.println("file empty");
				contact.setImage("contact.png");
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// try-with-resources to ensure the InputStream is closed
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

			// success message
			session.setAttribute("message", new Message("contact is added successfully", "success"));
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();

			// data not added error message
			session.setAttribute("message", new Message("something went wrong", "danger"));

		}

		return "normal/add_contact_form";
	}

	// show contacts handler
	// per page=5
	// current page = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "show user contacts");

		if (page == null || page < 0) {
	        page = 0; // Default to the first page (0-based)
	    }
		
		// to send list of all contacts...
		String userName = principal.getName();// to get the username of loggedin person
		User user = this.userRepository.getUserByUserName(userName);

		// will fetch all the contacts of a logged in person...
		Pageable pageable = PageRequest.of(page, 5); // Pageable is a parent interface of PageRequest
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);

		model.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show_contacts";
	}

	// showing particular contact details.......
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("CID " + cId);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		// solving security bug
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";
	}

	// delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		User user = this.userRepository.getUserByUserName(principal.getName());

		user.getContacts().remove(contact);
		
		this.userRepository.save(user);
		System.out.println("deleted");

		session.setAttribute("message", new Message("Contact deleted successfully", "success"));
		return "redirect:/user/show-contacts/0";
	}

	// open update form handler
	@PostMapping("/update-contact/{cId}") // GetMapping ko koi bhi url se open kar sakta h isiliye we used PostMapping
	public String updateForm(@PathVariable("cId") Integer cId, Model model) 
	{
		model.addAttribute("title", "Update Contact");

		Contact contact = this.contactRepository.findById(cId).get();

		model.addAttribute("contact", contact);
		return "normal/update_form";
	}

	// update contact handler
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {
		try 
		{
			// old contact detail
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

			// image
			if (!file.isEmpty())
			{
				//delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
				
				
				// update new image
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());
			} 
			else
			{
				contact.setImage(oldContactDetail.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact); 

			session.setAttribute("message", new Message("contact updated successfully.", "success"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "redirect:/user/"+contact.getcId()+"/contact"; 
	}
	
	//user profile handler(your profile)
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title", "profile page");
		return "normal/profile";
	}
	
	//open settings handler
	@GetMapping("/settings")
	public String openSettings()
	{
		return "normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session)
	{
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);

		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("password updated successfully....", "success"));
		}
		else
		{
			session.setAttribute("message", new Message("Incorrect old password!!!", "danger")); 
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
	
}
