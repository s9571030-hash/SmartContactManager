package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	//email id form handler
	@Autowired
	private EmailService emailService;
	
	@RequestMapping("/forgot")
	
	public String openEmailForm() {
		
		
		
		return"forgot_email_form";
	}
	
	//otp handller
	
	@PostMapping("/send-otp")
	
	public String sendotp(@RequestParam("email")String email, HttpSession session) {
		
		System.out.println("email=="+ email);
	Random random = new Random();
	int otp= 10000+random.nextInt(999999);
	
	System.out.println("otp=="+otp);
	
	//write code to send opt on email
	String subject="OTP FROM SCM";
	String from = "OTP=="+otp+"";
	String to= email;
	
	boolean flag =this.emailService.sendEmail(subject, from, to);
	if(flag) {
		session.setAttribute("otp", otp);
		
		return"verify_otp";
		
	}
	else {
		session.setAttribute("message","check your email id!!");
		return"forgot_email_form";
	}
		
	}
	

}
