package com.ederfmatos.reactive.kotlin

import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class User(val id: String, val name: String)

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping
    fun getUser(): One<User> {
        return One.from(User(UUID.randomUUID().toString(), "Eder"))
            .onNext { println("Usuário encontrado $it") }
            .filterNot { it.name == "Eder 1" }
            .onNext { println("Usuário encontrado 2 $it") }
            .flatMap { One.empty<User>() }
            .onEmpty { println("Não existe usuário") }
            .ifEmptyReturn { User("Usuário Mockado", "Eder Mock") }
            .onComplete { println("Finalizou") }
    }

}