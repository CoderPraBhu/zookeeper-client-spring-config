package com.coderprabhu.zkclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/hello/{prop}")
	public String hello(@PathVariable String prop) {
		System.out.println("Looking for: " + prop);
		return environment.getProperty(prop);
	}

}
