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

/** 群組權限對應資料 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(
    name = "user_group_scope",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "pk_user_group_scope",
          columnNames = {"id"}),
      @UniqueConstraint(
          name = "uk_user_group_scope",
          columnNames = {"user_group_id", "resource_scope_id"})
    })
public class UserGroupScopeEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String userGroupId;
  private String resourceScopeId;
  private LocalDateTime createdDate;
  private String createdBy;
  private LocalDateTime lastModifiedDate;
  private String lastModifiedBy;

  public UserGroupScopeEntity() {}

  public UserGroupScopeEntity(UserGroupScopeEntity value) {
    this.id = value.id;
    this.userGroupId = value.userGroupId;
    this.resourceScopeId = value.resourceScopeId;
    this.createdDate = value.createdDate;
    this.createdBy = value.createdBy;
    this.lastModifiedDate = value.lastModifiedDate;
    this.lastModifiedBy = value.lastModifiedBy;
  }

  public UserGroupScopeEntity(
      String id,
      String userGroupId,
      String resourceScopeId,
      LocalDateTime createdDate,
      String createdBy,
      LocalDateTime lastModifiedDate,
      String lastModifiedBy) {
    this.id = id;
    this.userGroupId = userGroupId;
    this.resourceScopeId = resourceScopeId;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastModifiedDate = lastModifiedDate;
    this.lastModifiedBy = lastModifiedBy;
  }

  /** Getter for <code>public.user_group_scope.id</code>. */
  @Id
  @Column(name = "id", nullable = false, length = 10)
  @NotNull
  @Size(max = 10)
  public String getId() {
    return this.id;
  }

  /** Setter for <code>public.user_group_scope.id</code>. */
  public void setId(String id) {
    this.id = id;
  }

  /** Getter for <code>public.user_group_scope.user_group_id</code>. 群組ID */
  @Column(name = "user_group_id", nullable = false, length = 10)
  @NotNull
  @Size(max = 10)
  public String getUserGroupId() {
    return this.userGroupId;
  }

  /** Setter for <code>public.user_group_scope.user_group_id</code>. 群組ID */
  public void setUserGroupId(String userGroupId) {
    this.userGroupId = userGroupId;
  }

  /** Getter for <code>public.user_group_scope.resource_scope_id</code>. 權限ID */
  @Column(name = "resource_scope_id", nullable = false, length = 10)
  @NotNull
  @Size(max = 10)
  public String getResourceScopeId() {
    return this.resourceScopeId;
  }

  /** Setter for <code>public.user_group_scope.resource_scope_id</code>. 權限ID */
  public void setResourceScopeId(String resourceScopeId) {
    this.resourceScopeId = resourceScopeId;
  }

  /** Getter for <code>public.user_group_scope.created_date</code>. */
  @Column(name = "created_date", nullable = false, precision = 6)
  @NotNull
  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  /** Setter for <code>public.user_group_scope.created_date</code>. */
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /** Getter for <code>public.user_group_scope.created_by</code>. */
  @Column(name = "created_by", nullable = false, length = 36)
  @NotNull
  @Size(max = 36)
  public String getCreatedBy() {
    return this.createdBy;
  }

  /** Setter for <code>public.user_group_scope.created_by</code>. */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /** Getter for <code>public.user_group_scope.last_modified_date</code>. */
  @Column(name = "last_modified_date", precision = 6)
  public LocalDateTime getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  /** Setter for <code>public.user_group_scope.last_modified_date</code>. */
  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /** Getter for <code>public.user_group_scope.last_modified_by</code>. */
  @Column(name = "last_modified_by", length = 36)
  @Size(max = 36)
  public String getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  /** Setter for <code>public.user_group_scope.last_modified_by</code>. */
  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
}
