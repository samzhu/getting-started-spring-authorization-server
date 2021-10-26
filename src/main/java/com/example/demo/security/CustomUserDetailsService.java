package com.example.demo.security;


import com.example.demo.entites.UserAccountEntity;
import com.example.demo.entites.UserGroupEntity;
import com.example.demo.entites.UserGroupMemberEntity;
import com.example.demo.repositories.UserAccountEntityRepository;
import com.example.demo.repositories.UserGroupEntityRepository;
import com.example.demo.repositories.UserGroupMemberEntityRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserAccountEntityRepository userAccountRepository;
  private UserGroupMemberEntityRepository userGroupMemberRepository;
  private UserGroupEntityRepository userGroupRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug(">> CustomUserDetailsService.loadUserByUsername username={}", username);
    User userDetails = null;
    Optional<UserAccountEntity> userOptional = userAccountRepository.findByUserName(username);
    // Not found...
    userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    UserAccountEntity user = userOptional.get();
    List<UserGroupMemberEntity> userGroupMembers =
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
              .map(UserGroupMemberEntity::getUserGroupId)
              .collect(Collectors.toList());
      List<UserGroupEntity> userGroups = userGroupRepository.findByIdIn(userGroupIds);
      Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
      for (UserGroupEntity userGroup : userGroups) {
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
