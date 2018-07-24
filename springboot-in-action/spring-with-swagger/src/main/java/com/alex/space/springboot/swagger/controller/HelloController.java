package com.alex.space.springboot.swagger.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex
 * Created by Alex on 2018/7/24.
 */
@RestController
public class HelloController {

	@ApiOperation(value = "Hello #{somebody}",
			notes = "return welcome ",
			tags = "Hello1"
	)
	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
	public String hello1(@ApiParam(value = "姓名参数") @PathVariable("name") String userName) {
		return "Welcome " + userName;
	}

	@ApiOperation(value = "Hello #{somebody}",
			notes = "return welcome ",
			tags = "Hello2"
	)
	@GetMapping(value = "/hello")
	public String hello2(@ApiParam(value = "姓名参数") @RequestParam("name") String userName) {
		return "Welcome " + userName;
	}

}
