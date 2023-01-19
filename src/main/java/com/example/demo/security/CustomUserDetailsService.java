package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.demo.infrastructure.repositories.UserAccountRepository;
import com.example.demo.infrastructure.repositories.UserGroupMemberRepository;
import com.example.demo.infrastructure.repositories.UserGroupRepository;
import com.example.demo.infrastructure.repositories.tables.pojos.UserAccount;
import com.example.demo.infrastructure.repositories.tables.pojos.UserGroup;
import com.example.demo.infrastructure.repositories.tables.pojos.UserGroupMember;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserAccountRepository userAccountRepository;
  private UserGroupMemberRepository userGroupMemberRepository;
  private UserGroupRepository userGroupRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug(">> CustomUserDetailsService.loadUserByUsername username={}", username);
    User userDetails = null;
    Optional<UserAccount> userOptional = userAccountRepository.findByUserName(username);
    // Not found...
    userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    UserAccount user = userOptional.get();
    List<UserGroupMember> userGroupMembers =
        userGroupMemberRepository.findByUserId(user.getId());
    if (userGroupMembers.size() == 0) {
      userDetails =
          new User(
              user.getUserName(),
              user.getUserPassword(),
              user.getEnabled(), // 是否可用
              !user.getAccountNonExpired(), // 是否過期
              !user.getCredentialsNonExpired(), // 證書不過期為true
              !user.getAccountNonLocked(), // 帳號未鎖定為true
              AuthorityUtils.NO_AUTHORITIES);
    } else {
      List<String> userGroupIds =
          userGroupMembers.stream()
              .map(UserGroupMember::getUserGroupId)
              .collect(Collectors.toList());
      List<UserGroup> userGroups = userGroupRepository.findByIdIn(userGroupIds);
      Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
      for (UserGroup userGroup : userGroups) {
        grantedAuthorities.add(new SimpleGrantedAuthority(userGroup.getCode()));
      }
      userDetails =
          new User(
              user.getUserName(),
              user.getUserPassword(),
              user.getEnabled(), // 是否可用
              !user.getAccountNonExpired(), // 是否過期
              !user.getCredentialsNonExpired(), // 證書不過期為true
              !user.getAccountNonLocked(), // 帳號未鎖定為true
              grantedAuthorities);
    }
    log.debug("<< CustomUserDetailsService.loadUserByUsername User={}", userDetails);
    return userDetails;
  }
}
