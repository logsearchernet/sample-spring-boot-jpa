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
import com.sample.model.UsersEntity;
import com.sample.service.RegisterService;

@Controller
public class MainController {
	
	private static Log logger = LogFactory.getLog(MainController.class);
	
	@Autowired
	private RegisterService registerService;

	@RequestMapping(value="/")
	public String index(){
		logger.info("-- INDEX --");
		return "index";
	}
	
	@RequestMapping(value="/registerForm")
	public String registerForm(RegisterForm registerForm){
		logger.info("-- REGISTER FORM --");
		return "registerForm";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, @Valid RegisterForm registerForm, BindingResult bindingResult){
		logger.info("-- REGISTER --");
		if (bindingResult.hasErrors()) {
            return "/registerForm";
        }
		
		UsersEntity user = registerService.findByEmail(registerForm.getEmail());
		
		if (user == null){
			String role = "ROLE_USER";
			registerService.register(registerForm.getEmail(), registerForm.getPassword(), role);
			return "redirect:/registerForm?success=1";
		} else {
			bindingResult.rejectValue("email", "error.exist.email", "An account already exists for this email.");
			return "/registerForm";
		}
	}
}
