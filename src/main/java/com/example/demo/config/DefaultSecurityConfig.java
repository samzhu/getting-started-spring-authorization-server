package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

  // @formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    // http
		// 	.authorizeRequests(authorizeRequests ->
		// 		authorizeRequests.anyRequest().authenticated()
		// 	)
		// 	.formLogin(org.springframework.security.config.Customizer.withDefaults());
		// return http.build();

    http.authorizeRequests()
        .antMatchers("/images/*")
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .permitAll()
        .and()
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());
    return http.build();
  }
  // @formatter:on

}
