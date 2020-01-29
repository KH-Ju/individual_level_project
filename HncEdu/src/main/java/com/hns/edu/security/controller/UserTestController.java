package com.hns.edu.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hns.edu.security.domain.Member;
import com.hns.edu.security.service.UserTestService;

@Controller
public class UserTestController {

	@Autowired
	UserTestService userService;
	
	@RequestMapping("/openapi/readUser/{username}")
	public @ResponseBody String openApiReadUser(@PathVariable String username) {
		Member member = userService.readUser(username);
		return member.getName();
	}
	
	@RequestMapping("/openapi/readAuthority/{username}")
	public @ResponseBody String opanApiReadQuthority(@PathVariable String username) {
		List<String> auths = userService.readAuthority(username);
		
		StringBuffer buf = new StringBuffer();
		for(String auth : auths) {
			buf.append(auth);
			buf.append(" ");
		}
		
		return buf.toString();
	}
}
