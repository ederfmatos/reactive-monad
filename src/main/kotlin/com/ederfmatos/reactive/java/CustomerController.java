package com.ederfmatos.reactive.java;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    public record Customer(String id, String name) {
    }

    @GetMapping
    public One<Customer> list() {
        return One.from(new Customer(UUID.randomUUID().toString(), "Eder"))
                .onNext(user -> System.out.println("Cliente encontrado" + user))
                .filterNot(user -> user.name().equals("Eder 1"))
                .onNext(user -> System.out.println("Cliente encontrado 2" + user))
                .flatMap(user -> One.<Customer>empty())
                .onEmpty(() -> System.out.println("Não existe usuário"))
                .ifEmptyReturn(() -> new Customer("Cliente Mockado", "Eder Mock"))
                .onComplete(() -> System.out.println("Finalizou"));
    }

}