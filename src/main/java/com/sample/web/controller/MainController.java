package com.sample.web.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sample.service.FileService;

@Controller
@RequestMapping("/")
public class MainController {
	
	private static Log logger = LogFactory.getLog(MainController.class);

	//public static String uploadFilePath;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value="/")
	public String index(){
		logger.info("-- INDEX --");
		return "index";
	}
	
	
	@RequestMapping(value="/form")
	public String form(){
		return "form";
	}
	
	@RequestMapping(value="/form2")
	public String form2(){
		return "form2";
	}
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request) throws IOException, ServletException{
    	Part filePart = request.getPart("file");
		
	    
     	String name = filePart.getSubmittedFileName();
     	InputStream input = filePart.getInputStream();
     	byte[] b = IOUtils.readBytesAndClose(input, 0);
    	fileService.save(name, b);
	    
	    return "success";
    }

}
