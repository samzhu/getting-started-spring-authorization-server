/*
 * This file is generated by jOOQ.
 */
package com.example.demo.entites;


import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** 帳號資料 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(
    name = "user_account",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "pk_user_account",
          columnNames = {"id"}),
      @UniqueConstraint(
          name = "uk_user_account",
          columnNames = {"user_name"})
    })
public class UserAccountEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String userName;
  private String userPassword;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;
  private LocalDateTime createdDate;
  private String createdBy;
  private LocalDateTime lastModifiedDate;
  private String lastModifiedBy;

  public UserAccountEntity() {}

  public UserAccountEntity(UserAccountEntity value) {
    this.id = value.id;
    this.userName = value.userName;
    this.userPassword = value.userPassword;
    this.enabled = value.enabled;
    this.accountNonExpired = value.accountNonExpired;
    this.accountNonLocked = value.accountNonLocked;
    this.credentialsNonExpired = value.credentialsNonExpired;
    this.createdDate = value.createdDate;
    this.createdBy = value.createdBy;
    this.lastModifiedDate = value.lastModifiedDate;
    this.lastModifiedBy = value.lastModifiedBy;
  }

  public UserAccountEntity(
      String id,
      String userName,
      String userPassword,
      Boolean enabled,
      Boolean accountNonExpired,
      Boolean accountNonLocked,
      Boolean credentialsNonExpired,
      LocalDateTime createdDate,
      String createdBy,
      LocalDateTime lastModifiedDate,
      String lastModifiedBy) {
    this.id = id;
    this.userName = userName;
    this.userPassword = userPassword;
    this.enabled = enabled;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastModifiedDate = lastModifiedDate;
    this.lastModifiedBy = lastModifiedBy;
  }

  /** Getter for <code>public.user_account.id</code>. */
  @Id
  @Column(name = "id", nullable = false, length = 36)
  @NotNull
  @Size(max = 36)
  public String getId() {
    return this.id;
  }

  /** Setter for <code>public.user_account.id</code>. */
  public void setId(String id) {
    this.id = id;
  }

  /** Getter for <code>public.user_account.user_name</code>. 用戶帳號 */
  @Column(name = "user_name", nullable = false, length = 30)
  @NotNull
  @Size(max = 30)
  public String getUserName() {
    return this.userName;
  }

  /** Setter for <code>public.user_account.user_name</code>. 用戶帳號 */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /** Getter for <code>public.user_account.user_password</code>. 用戶密碼 */
  @Column(name = "user_password", nullable = false, length = 60)
  @NotNull
  @Size(max = 60)
  public String getUserPassword() {
    return this.userPassword;
  }

  /** Setter for <code>public.user_account.user_password</code>. 用戶密碼 */
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  /** Getter for <code>public.user_account.enabled</code>. 是否可用 */
  @Column(name = "enabled", nullable = false)
  @NotNull
  public Boolean getEnabled() {
    return this.enabled;
  }

  /** Setter for <code>public.user_account.enabled</code>. 是否可用 */
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  /** Getter for <code>public.user_account.account_non_expired</code>. 是否過期 */
  @Column(name = "account_non_expired", nullable = false)
  @NotNull
  public Boolean getAccountNonExpired() {
    return this.accountNonExpired;
  }

  /** Setter for <code>public.user_account.account_non_expired</code>. 是否過期 */
  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  /** Getter for <code>public.user_account.account_non_locked</code>. 帳號是否鎖定 */
  @Column(name = "account_non_locked", nullable = false)
  @NotNull
  public Boolean getAccountNonLocked() {
    return this.accountNonLocked;
  }

  /** Setter for <code>public.user_account.account_non_locked</code>. 帳號是否鎖定 */
  public void setAccountNonLocked(Boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  /** Getter for <code>public.user_account.credentials_non_expired</code>. 證書是否過期 */
  @Column(name = "credentials_non_expired", nullable = false)
  @NotNull
  public Boolean getCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  /** Setter for <code>public.user_account.credentials_non_expired</code>. 證書是否過期 */
  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  /** Getter for <code>public.user_account.created_date</code>. */
  @Column(name = "created_date", nullable = false, precision = 6)
  @NotNull
  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  /** Setter for <code>public.user_account.created_date</code>. */
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /** Getter for <code>public.user_account.created_by</code>. */
  @Column(name = "created_by", nullable = false, length = 36)
  @NotNull
  @Size(max = 36)
  public String getCreatedBy() {
    return this.createdBy;
  }

  /** Setter for <code>public.user_account.created_by</code>. */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /** Getter for <code>public.user_account.last_modified_date</code>. */
  @Column(name = "last_modified_date", nullable = false, precision = 6)
  @NotNull
  public LocalDateTime getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  /** Setter for <code>public.user_account.last_modified_date</code>. */
  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /** Getter for <code>public.user_account.last_modified_by</code>. */
  @Column(name = "last_modified_by", nullable = false, length = 36)
  @NotNull
  @Size(max = 36)
  public String getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  /** Setter for <code>public.user_account.last_modified_by</code>. */
  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
}
