package com.gamesup.api.config;

import com.gamesup.api.security.JwtFilter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest(excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public @interface WebMvcTestNoSecurity {
    Class<?>[] controllers() default {};
}