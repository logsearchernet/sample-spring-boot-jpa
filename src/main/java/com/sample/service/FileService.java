package com.sample.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sample.dao.FileDao;

@Component
public class FileService {

	@Autowired
	private FileDao fileDao;
	
	@Transactional
	public void save(String name, String filename, byte[] b){
		fileDao.save(name, filename, b);
	}
}
