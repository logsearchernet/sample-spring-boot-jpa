package com.sample.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sample.form.RegisterForm;
import com.sample.service.RegisterService;

@Controller
@RequestMapping("/user/")
public class UserController { 
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@Autowired
	private RegisterService registerService;
	

	@RequestMapping(value="dashboard")
	public String index(){
		logger.info("-- DASHBOARD --");
		return "user/dashboard";
	}
	
	@RequestMapping(value="myAccountForm")
	public String myAccountForm(RegisterForm form){
		logger.info("-- MY ACCOUNT FORM --");
		return "user/myAccountForm";
	}
	
	@RequestMapping(value="myAccount", method = RequestMethod.POST)
	public String myAccount(HttpServletRequest request, @Valid RegisterForm registerForm, BindingResult bindingResult){
		logger.info("-- MY ACCOUNT --");
		if (bindingResult.hasErrors()) {
            return "user/myAccountForm";
        }
		
		String role = "ROLE_USER";
		registerService.register(registerForm.getEmail(), registerForm.getPassword(), role);
		return "redirect:myAccountForm?success=1";
	}
}
