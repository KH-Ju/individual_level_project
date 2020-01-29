package com.hns.edu.security.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.hns.edu.security.domain.Member;

public class SecurityMember extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ip;
	
	public SecurityMember(Member member) {
		super(member.getUsername(), member.getPassword(), member.getAuthorities());
		// TODO Auto-generated constructor stub
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

}
