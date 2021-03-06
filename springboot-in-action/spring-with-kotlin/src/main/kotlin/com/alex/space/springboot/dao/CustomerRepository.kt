package com.alex.space.springboot.dao

import com.alex.space.springboot.model.Customer
import org.springframework.data.repository.CrudRepository

/**
 * Created by Alex on 2017/7/5.
 */
interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByLastName(name: String): List<Customer>
}