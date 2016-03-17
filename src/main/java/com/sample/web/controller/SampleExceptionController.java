package com.sample.web.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sample.excpetion.CustomGenericException;

@Controller
@RequestMapping("/ex/")
public class SampleExceptionController extends BaseController{

	@RequestMapping(value = "/{type:.+}", method = RequestMethod.GET)
	public ModelAndView getPages(@PathVariable("type") String type)
		throws Exception {

	  if ("error".equals(type)) {
		// go handleCustomException
		throw new CustomGenericException("E888", "This is custom message");
	  } else if ("io-error".equals(type)) {
		// go handleAllException
		throw new IOException();
	  } else {
		return new ModelAndView("/ex/index").addObject("msg", type);
	  }

	}

	
}
