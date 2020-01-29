package com.hns.edu.security.service.Adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.hns.edu.security.handlers.CustomLoginSuccessHandler;
import com.hns.edu.security.service.CustomUserDetailsService;

@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("passwordEncoder()");
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		System.out.println("successHandler()");
		return new CustomLoginSuccessHandler("/");
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		System.out.println("configure(WebSecurity web)");
		// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
		web.ignoring().antMatchers("/openapi/**", "/resources/static/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("configure(HttpSecurity http)");
		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().formLogin().successHandler(successHandler());
		
		/*
		http.authorizeRequests()
        // 페이지 권한 설정
			.antMatchers("/admin/**").hasRole("ADMIN")
	        .antMatchers("/user/myinfo").hasRole("MEMBER")
	        .antMatchers("/**").permitAll()
    	.and() // 로그인 설정
			.formLogin()
        	.loginPage("/user/login")
        	.defaultSuccessUrl("/user/login/result")
        	.permitAll()
		.and() // 로그아웃 설정
	        .logout()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	        .logoutSuccessUrl("/user/logout/result")
	        .invalidateHttpSession(true)
    	.and()
        	// 403 예외처리 핸들링
			.exceptionHandling().accessDeniedPage("/user/denied");
		 */
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		System.out.println("configure(AuthenticationManagerBuilder auth)");
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
}
