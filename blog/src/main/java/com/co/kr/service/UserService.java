package com.co.kr.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co.kr.repository.UserRepository;
import com.co.kr.model.RoleType;
import com.co.kr.model.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user;
	}
	
	@Transactional
	public int 회원가입(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		try {
			userRepository.save(user);
			return 1;
		} catch (Exception e) {
			return -1;
		}
		
	}

	@Transactional
	public void 회원수정(User user) {
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원 찾기 실패");
		});
		
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}
	}
}
