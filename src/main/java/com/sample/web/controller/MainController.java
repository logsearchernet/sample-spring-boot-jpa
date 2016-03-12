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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.form.FileForm;
import com.sample.service.FileService;

@Controller
@RequestMapping("/")
public class MainController {
	
	private static Log logger = LogFactory.getLog(MainController.class);

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value="/")
	public String index(){
		logger.info("-- INDEX --");
		return "index";
	}
	
	
	@RequestMapping(value="/form")
	public String form(FileForm fileForm, @RequestParam(required=false) String success){
		
		return "form";
	}
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request, @Valid FileForm fileForm, BindingResult bindingResult) throws IOException, ServletException{
    	
    	if (bindingResult.hasErrors()) {
			// Retrieve the error message MANUAL
			for (Object object : bindingResult.getAllErrors()) {
			    if(object instanceof FieldError) {
			        FieldError fieldError = (FieldError) object;

			        String message = messageSource.getMessage(fieldError, null);
			        logger.info("message=>"+message);
			    }
			}
			
            return "form";
        }
    	
    	Part filePart = request.getPart("file");
		String filename = filePart.getSubmittedFileName();
     	InputStream input = filePart.getInputStream();
     	byte[] b = IOUtils.readBytesAndClose(input, 0);
    	fileService.save(fileForm.getName(), filename, b);
	    
	    return "redirect:/form?success=1";
    }

    
}
