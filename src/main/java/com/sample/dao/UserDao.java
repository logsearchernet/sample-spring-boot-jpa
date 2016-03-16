package com.sample.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sample.model.UsersEntity;

@Repository
public class UserDao extends BaseDao<UsersEntity, String> {

	
	public List<UsersEntity> loadPaginationHere(int pageNumber, int pageSize){
		String q = "SELECT s FROM "+UsersEntity.class.getName()+" s";
		return super.loadPagination(q, pageNumber, pageSize);
	}
}
