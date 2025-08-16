package com.example.courses.nameservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// Custom annotation to mark Feign clients
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@FeignClient
public @interface NameServiceClient {
    String name() default "";
}

// Feign client interface for the name service
@NameServiceClient(name = "name-service")
interface NameService {
    @GetMapping("/")
    String getName();
}