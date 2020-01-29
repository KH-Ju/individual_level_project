package com.hns.edu.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hns.edu.db.mapper.UserMapper;
import com.hns.edu.security.domain.Member;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final String ROLE_PREFIX = "ROLE_";
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("loadUserByUsername()");
		Member member = userMapper.readUser(username);
		if(member != null) {
			member.setAuthorities(makeGrantedAuthority(userMapper.readAuthority(username)));
		}
		return new SecurityMember(member);
		
	}
	
	 private static List<GrantedAuthority> makeGrantedAuthority(List<String> roles){
		 List<GrantedAuthority> list = new ArrayList<>();
		 roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX+role)));
		 return list;
	 }


	

}
