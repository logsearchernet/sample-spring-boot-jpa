package com.sample.web.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.form.FileForm;
import com.sample.service.FileService;

@Controller
@RequestMapping("/admin/")
public class AdminController { 
	
	private static Log logger = LogFactory.getLog(AdminController.class);
	
	@Autowired
	private FileService fileService;
	
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

}
