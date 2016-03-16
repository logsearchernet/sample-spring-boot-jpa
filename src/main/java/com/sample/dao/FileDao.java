package com.sample.dao;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sample.model.FileEntity;

@Repository
public class FileDao extends BaseDao<FileEntity, String> {

	private static Log logger = LogFactory.getLog(FileDao.class);
	
	public void save(String name, String filename, byte[] b){
		String id = UUID.randomUUID().toString();
		FileEntity f = new FileEntity();
		f.setId(id);
		f.setName(name);
		f.setFilename(filename);
		f.setFile(b);
		create(f);
		
		logger.info("ID=>"+id);
		
		
	}
	
	public Long count(){
		long count = countWithNamedQuery("file.count", null);
		logger.info("COUNT=>"+ count);
		return count;
	}
}
