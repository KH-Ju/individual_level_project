package com.hns.edu.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hns.edu.db.mapper.UserMapper;
import com.hns.edu.security.domain.Member;

@Service
public class UserTestService {
	
	@Autowired
	UserMapper userMapper;
	
	public Member readUser(String username) {
		return userMapper.readUser(username);
	}
	
	public List<String> readAuthority(String username){
		return userMapper.readAuthority(username);
	}
	
	
}
