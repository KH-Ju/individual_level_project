package com.hns.edu.db.mapper;

import java.util.List;

import com.hns.edu.security.domain.Member;

public interface UserMapper {
	
	public Member readUser(String username);
	
	public List<String> readAuthority(String username);
	
	
}
