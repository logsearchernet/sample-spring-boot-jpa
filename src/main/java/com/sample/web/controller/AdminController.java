package com.sample.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.excpetion.CustomGenericException;
import com.sample.form.FileForm;
import com.sample.form.UserForm;
import com.sample.form.UserPasswordForm;
import com.sample.model.UsersEntity;
import com.sample.service.FileService;
import com.sample.service.UserService;

@Controller
@RequestMapping("/admin/")
public class AdminController extends BaseController { 
	
	private static Log logger = LogFactory.getLog(AdminController.class);
	
	private static Set<String> roleList = new HashSet<String>();
	static {
		roleList.add("ROLE_ADMIN");
		roleList.add("ROLE_USER");
		
	}
	
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_CREATE = "create";
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="dashboard")
	public String index(){
		logger.info("-- DASHBOARD --");
		return "/admin/dashboard";
	}
	
	
	@RequestMapping(value="form")
	public String form(HttpServletRequest request, FileForm fileForm, @RequestParam(required=false) String success){

    	Long count = fileService.count();
    	request.setAttribute("count", count);
		return "/admin/form";
	}
    
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request, @Valid FileForm fileForm, BindingResult bindingResult) throws IOException, ServletException{
    	
    	if (bindingResult.hasErrors()) {
    		Long count = fileService.count();
        	request.setAttribute("count", count);
            return "/admin/form";
        }
    	
    	Part filePart = request.getPart("file");
		String filename = filePart.getSubmittedFileName();
     	InputStream input = filePart.getInputStream();
     	byte[] b = IOUtils.readBytesAndClose(input, 0);
    	fileService.save(fileForm.getName(), filename, b);
    	
	    
	    return "redirect:/admin/form?success=1";
    }

    @RequestMapping(value="userList")
	public String userList(HttpServletRequest request){
    	
    	int pageNumber = 1;
    	int pageSize = 10;
    	
    	List<UserForm> userFormList = userService.findUser(pageNumber, pageSize);
    	request.setAttribute("userFormList", userFormList);

		return "/admin/userList";
	}
    
    @RequestMapping(value="userForm")
	public String userForm(HttpServletRequest request, Model model, @RequestParam(required=false) String email, @RequestParam(required=false) boolean edit){
		logger.info("-- USER FORM --, email=>"+email);
		
		if (StringUtils.isEmpty(email)){
			throw new CustomGenericException("E100", "Email parameter cannot be empty.");
		}
		
		UserForm form = null;
		if (email != null) {
			form = userService.findById(email);
		}
		if (form == null){
			form = new UserForm();
		}
		
		form.setAction(edit?ACTION_EDIT:ACTION_CREATE);
		model.addAttribute("userForm", form);
		model.addAttribute("roleList", roleList);
		model.addAttribute("action", edit?ACTION_EDIT:ACTION_CREATE);
		
		return "admin/userForm";
	}
	
	@RequestMapping(value="user", method = RequestMethod.POST)
	public String user(HttpServletRequest request, Model model, @Valid UserForm userForm, BindingResult bindingResult){
		logger.info("-- USER --");
		String email = userForm.getEmail();
		String action = userForm.getAction(); 
		boolean bEdit = (action.equals(ACTION_EDIT));
		boolean bCreate = (action.equals(ACTION_CREATE));
		model.addAttribute("roleList", roleList);
		if (bindingResult.hasErrors()) {
            return "admin/userForm";
        }
		
		if (bCreate && !userForm.getPassword().equals(userForm.getPassword2())){
			bindingResult.rejectValue("password", "error.password", "Password not match.");
			return "admin/userForm";
		}
		
		if (bCreate) {
			UsersEntity user = userService.findEntityById(email);
			if (user != null){
				bindingResult.rejectValue("email", "error.exist.email", "An account already exists for this email.");
				return "admin/userForm";
			}
			userService.save(userForm);
			return "redirect:userForm?email="+email+"&success=1";
		} else if (bEdit) {
			UserForm user = userService.findById(email);
			user.setEmail(email);
			user.setEnabled(userForm.isEnabled());
			user.setRoles(userForm.getRoles());
			userService.save(user);
			return "redirect:userForm?email="+email+"&success=1";
		}
		return "admin/userForm";
		
	}
	
	@RequestMapping(value = "userPasswordForm")
	public String userPasswordForm(HttpServletRequest request, Model model,
			@RequestParam(required = true) String email) {
		logger.info("-- USER PASSWORD FORM --, email=>" + email);
		UserPasswordForm form = new UserPasswordForm();
		form.setEmail(email);
		model.addAttribute("userPasswordForm", form);
		return "admin/userPasswordForm";
	}
	
	@RequestMapping(value="userPassword", method = RequestMethod.POST)
	public String userPassword(HttpServletRequest request, Model model, @Valid UserPasswordForm userPasswordForm, BindingResult bindingResult){
		String email = userPasswordForm.getEmail();
		logger.info("-- USER PASSWORD FORM --, email=>" + email);
		if (bindingResult.hasErrors()) {
            return "admin/userPasswordForm";
        }
		
		String passwordOld = userPasswordForm.getPasswordOld();
		String password = userPasswordForm.getPassword();
		String password2 = userPasswordForm.getPassword2();
		UsersEntity user = userService.findEntity4UserValidate(email, passwordOld);
		if (user == null) {
			bindingResult.rejectValue("email", "error.notmatch.emailpassword");
			return "admin/userPasswordForm";
		} else if (!password.equals(password2)){
			bindingResult.rejectValue("password", "error.notmatch.passwordconfirm");
			return "admin/userPasswordForm";
		} else {
			user.setPassword(password);
			userService.updateUserEntity(user);
		}
		
		return "redirect:userPasswordForm?email="+email+"&success=1";
	}
}
