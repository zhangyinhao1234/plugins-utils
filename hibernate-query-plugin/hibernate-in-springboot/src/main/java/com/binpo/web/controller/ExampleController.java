package com.binpo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binpo.pojo.Example;
import com.binpo.web.service.IBIExampleService;

@RestController
public class ExampleController {
	@Autowired
	private IBIExampleService iBIExampleService;

	@RequestMapping("/example/hello")
	public Example hello() {
		Example objById = iBIExampleService.getObjById(1L);
		return objById;
	}
}
