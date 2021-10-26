package com.example.demo.security;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsAuthenticationProvider
    extends AbstractUserDetailsAuthenticationProvider {

  private UserDetailsService userDetailsService;
  private PasswordEncoder passwordEncoder;

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    log.debug(
        ">> CustomUserDetailsAuthenticationProvider.additionalAuthenticationChecks userDetails={}, authentication={}",
        userDetails,
        authentication);
    if (authentication.getCredentials() == null || userDetails.getPassword() == null) {
      throw new BadCredentialsException("Credentials may not be null.");
    }
    log.debug(
        "credentials={}, password={}", authentication.getCredentials(), userDetails.getPassword());
    if (!passwordEncoder.matches(
        (String) authentication.getCredentials(), userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password");
    }
    log.debug("<< CustomUserDetailsAuthenticationProvider.additionalAuthenticationChecks");
  }

  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    log.debug(
        ">> CustomUserDetailsAuthenticationProvider.retrieveUser username={}, authentication={}",
        username,
        authentication);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    log.debug(
        "<< CustomUserDetailsAuthenticationProvider.retrieveUser UserDetails={}", userDetails);
    return userDetails;
  }
}
