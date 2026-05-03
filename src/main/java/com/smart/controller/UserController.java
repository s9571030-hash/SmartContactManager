package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

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

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	//method for adding common data toresponse
	@ModelAttribute
	public void addCommonData(Model m,Principal principal) {
		String userName=	principal.getName();
		System.out.println("USERNAME"+userName);
		
		User user=userRepository.getUserByUserName(userName);
		System.out.println("User"+user);
		m.addAttribute("user", user);
		
	}
	
	//jai shree ram home dash board handller
	@RequestMapping("/index")
	public String dashboard(Model m,Principal principal) {
	
		return"normal/user_dashboard";
	}
	
	//open contact from handller
	@GetMapping("/add-contact")
	public String openAddContactFrom(Model m) {
		m.addAttribute("title", "Add-Contact");
		m.addAttribute("contact", new Contact());
	return "normal/add_contact_from";	
	}
	// creating a process data handler
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("imageFile") MultipartFile file,
			Principal principal,
			HttpSession session) {
		try {
			
		String name= principal.getName();
		User user= this.userRepository.getUserByUserName(name);
		//image uploading
			if(file.isEmpty()) {
				System.out.println("file is empty!!");
				contact.setImage("contact.png");
				
			} else {
				contact.setImage(file.getOriginalFilename());
				
		File filesave=new ClassPathResource("static/image").getFile();
		
	Path path=	Paths.get(filesave.getAbsolutePath()+File.separator+file.getOriginalFilename());
		
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
		System.out.println("image is uploaded successfully");
			}
		

		
		contact.setUser(user);
		
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		
		System.out.println("data added successfully");
		
		System.out.println("DATA"+ contact);
		
		System.out.println("image"+ file.getOriginalFilename());
		
		//success message
	session.setAttribute("message", new Message("Your Contact is added!! add more", "success"));
		
		}catch(Exception e) {
			
			e.printStackTrace();
			//error message
session.setAttribute("message", new Message("Something Went Wrong!! Try again", "danger"));
		}
		return"normal/add_contact_from";
	}
	//contact view handler
	//kitne page dikhana hai 5[n]
	// current page=0 
	
	@GetMapping("/show-contacts/{page}")
public String showContacts(@PathVariable("page")Integer page, Model m ,Principal principal) {
 
		m.addAttribute("title","Show  User Contacts");
	//contact ki list bhejni hai
		
		String userName=principal.getName();
	User user = this.userRepository.getUserByUserName(userName);
	//current page
	//perpage size
	Pageable pageable=PageRequest.of(page, 5);
		
	Page<Contact>contacts =	this.contactRepository.findContactsByUser(user.getId(),pageable);
	
	m.addAttribute("contacts", contacts);
	m.addAttribute("currentPage",page );
	m.addAttribute("totalPages",contacts.getTotalPages());
	
	return"normal/show_contacts";
}

// showing particular work details
	@RequestMapping("/{cid}/contact")
	public String showContactdetails(@PathVariable("cid") Integer cid,Model m,Principal principal) {
		
		System.out.println("CID=="+cid);
Optional<Contact>	contactOptional =	this.contactRepository.findById(cid);
 Contact contact = contactOptional.get();
 // provide accessbility only authorize person
String userName= principal.getName();
 User user=this.userRepository.getUserByUserName(userName);
 if(user.getId()==contact.getUser().getId()) {
	 m.addAttribute("contact", contact); 
	 m.addAttribute("title", contact.getName());
 }
 

	return "normal/contact_detail";	
	}
	//delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model m,Principal principal,HttpSession session) {

		Optional<Contact>contactOptional=this.contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		//check assisment
		String userName= principal.getName();
		 User user=this.userRepository.getUserByUserName(userName);
		if(user.getId()==contact.getUser().getId()) {
			//delete old photo
			
		User user1 =this.userRepository.getUserByUserName(principal.getName());
		user1.getContacts().remove(contact);
		this.userRepository.save(user1);
		session.setAttribute("message", new Message("Contact deleted Successfully!!", "success"));
		}
		
		return "redirect:/user/show-contacts/0";
	}
	//update handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model m) {
		
		m.addAttribute("title", "Update-Contact");
		
		Contact contact=this.contactRepository.findById(cid).get();
		
		m.addAttribute("contact", contact);
		
		return"normal/update_form";
	}
	//Update-processing handler
	@RequestMapping(value="/process-update",method=RequestMethod.POST )
	public String updatehandler(@ModelAttribute Contact contact,
			@RequestParam("imageFile") MultipartFile file,
			Model m,HttpSession session ,Principal principal) {
		//old contact details
	Contact oldcontactDetail = this.contactRepository.findById(contact.getCid()).get();
		//image
		try {
			if(!file.isEmpty()) {
			//file work
				
				//delete
				File deletefile=new ClassPathResource("static/image").getFile();
				File file1=new File(deletefile,oldcontactDetail.getImage());
				file1.delete();
				// update new photo
				
			File filesave=new ClassPathResource("static/image").getFile();
				
			Path path=	Paths.get(filesave.getAbsolutePath()+File.separator+file.getOriginalFilename());
					
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
			}
			else {
			
				contact.setImage(oldcontactDetail.getImage());
				
			}
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
		this.contactRepository.save(contact);
		session.setAttribute("message",new Message("your contact is updated", "success"));
		
		}catch(Exception e) {
			
			e.printStackTrace();
		
		}
		
		
		System.out.println("contact name"+ contact.getName());
		System.out.println("contact name"+ contact.getCid());
		
		return"redirect:/user/"+contact.getCid()+"/contact";
	}
	// your profile handler
	@GetMapping("/profile")
	 public String yourprofile( Model m) {
		
		m.addAttribute("title","Profile-Page");
		 return"normal/profile";
	 }
	
	// open setting handler
	@GetMapping("/settings")
	public String openSetting() {
		
		
		
		return "normal/settings";
	}
	//changepassword..handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, 
			Principal principal,HttpSession httpSession) {
		
		System.out.println("1pass"+oldPassword);
		System.out.println("2pass"+newPassword);
		
		String userName=principal.getName();
		User currentUser=this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())){
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			httpSession.setAttribute("message", new Message("YourPassword Successfully changed..","success"));
		}
		else {
			httpSession.setAttribute("message", new Message("Please enter your currect old password..","danger"));
		return"redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
	
	
	
	
	
	

}
