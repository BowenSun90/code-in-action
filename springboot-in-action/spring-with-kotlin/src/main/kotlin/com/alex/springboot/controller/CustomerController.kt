package com.alex.springboot.controller

import com.alex.springboot.dao.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by Alex on 2017/7/5.
 */
@RestController
class CustomerController @Autowired constructor(val repository: CustomerRepository) {

    @RequestMapping("/", method = arrayOf(RequestMethod.GET))
    fun findAll() = repository.findAll()

    @GetMapping("/{name}")
    fun findByLastName(@PathVariable name: String) = repository.findByLastName(name)

}