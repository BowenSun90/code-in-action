package com.alex.space.springboot.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


/**
 * Created by Alex on 2017/7/5.
 */
@Entity
data class Customer(
        var firstName: String = "",
        var lastName: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
)
