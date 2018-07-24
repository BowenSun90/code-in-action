package com.alex.space.springboot

import com.alex.space.springboot.dao.CustomerRepository
import com.alex.space.springboot.model.Customer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class SpringWithKotlinApplication {
    @Bean
    open fun init(repository: CustomerRepository): CommandLineRunner {
        return CommandLineRunner {
            repository.save(Customer("Jack", "Bauer"))
            repository.save(Customer("Chloe", "O'Brian"))
            repository.save(Customer("Kim", "Bauer"))
            repository.save(Customer("David", "Palmer"))
            repository.save(Customer("Michelle", "Dessler"))
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SpringWithKotlinApplication::class.java, *args)
}
