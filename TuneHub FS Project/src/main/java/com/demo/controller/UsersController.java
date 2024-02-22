package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.entities.Users;
import com.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController
{
	@Autowired
	UsersService userv;

	@PostMapping("/register")
	public String addUser(
			@ModelAttribute Users user) 
	{

		boolean userstatus = userv.emailExists(user.getEmail());
		if(userstatus == false)
		{
			userv.addUser(user);
			System.out.println("User is added");
			return "registersuccess";
		}

		else
		{
			System.out.println("User is already exist");
			return "registerfail";
		}



	}

	@PostMapping("/login")
	public String validateUser(@RequestParam String email, @RequestParam String password,
			HttpSession session)
	{

		if(userv.validateUser(email, password) == true)
		{

			session.setAttribute("email", email);
			if(userv.getRole(email) .equals("admin"))
			{
				return "adminhome";
			}
			else
			{
				return "customerhome";
			}
		}

		else
		{
			return "loginfail";
		}
	}
	
	
	@GetMapping("/exploresongs")
	public String exploresongs(HttpSession session)
	{
		String email=(String) session.getAttribute("email");
		Users user=userv.getUser(email);
		
		boolean userStatus=user.isPremium();
		if(userStatus==true)
		{
			return "premiumcustomer";
		}
		else
		{
			return "payment";
		}
	}
	
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidating session
        session.invalidate();
        return "index";
    
    }


}



