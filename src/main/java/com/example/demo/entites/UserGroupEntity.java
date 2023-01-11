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

/** 群組資料 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(
    name = "user_group",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "pk_user_group",
          columnNames = {"id"}),
      @UniqueConstraint(
          name = "uk_user_group",
          columnNames = {"name"})
    })
public class UserGroupEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String code;
  private String name;
  private LocalDateTime createdDate;
  private String createdBy;
  private LocalDateTime lastModifiedDate;
  private String lastModifiedBy;

  public UserGroupEntity() {}

  public UserGroupEntity(UserGroupEntity value) {
    this.id = value.id;
    this.code = value.code;
    this.name = value.name;
    this.createdDate = value.createdDate;
    this.createdBy = value.createdBy;
    this.lastModifiedDate = value.lastModifiedDate;
    this.lastModifiedBy = value.lastModifiedBy;
  }

  public UserGroupEntity(
      String id,
      String code,
      String name,
      LocalDateTime createdDate,
      String createdBy,
      LocalDateTime lastModifiedDate,
      String lastModifiedBy) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastModifiedDate = lastModifiedDate;
    this.lastModifiedBy = lastModifiedBy;
  }

  /** Getter for <code>public.user_group.id</code>. */
  @Id
  @Column(name = "id", nullable = false, length = 10)
  @NotNull
  @Size(max = 10)
  public String getId() {
    return this.id;
  }

  /** Setter for <code>public.user_group.id</code>. */
  public void setId(String id) {
    this.id = id;
  }

  /** Getter for <code>public.user_group.code</code>. 群組代碼 */
  @Column(name = "code", nullable = false, length = 20)
  @NotNull
  @Size(max = 20)
  public String getCode() {
    return this.code;
  }

  /** Setter for <code>public.user_group.code</code>. 群組代碼 */
  public void setCode(String code) {
    this.code = code;
  }

  /** Getter for <code>public.user_group.name</code>. 群組名稱 */
  @Column(name = "name", nullable = false, length = 50)
  @NotNull
  @Size(max = 50)
  public String getName() {
    return this.name;
  }

  /** Setter for <code>public.user_group.name</code>. 群組名稱 */
  public void setName(String name) {
    this.name = name;
  }

  /** Getter for <code>public.user_group.created_date</code>. */
  @Column(name = "created_date", nullable = false, precision = 6)
  @NotNull
  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  /** Setter for <code>public.user_group.created_date</code>. */
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /** Getter for <code>public.user_group.created_by</code>. */
  @Column(name = "created_by", nullable = false, length = 36)
  @NotNull
  @Size(max = 36)
  public String getCreatedBy() {
    return this.createdBy;
  }

  /** Setter for <code>public.user_group.created_by</code>. */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /** Getter for <code>public.user_group.last_modified_date</code>. */
  @Column(name = "last_modified_date", precision = 6)
  public LocalDateTime getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  /** Setter for <code>public.user_group.last_modified_date</code>. */
  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /** Getter for <code>public.user_group.last_modified_by</code>. */
  @Column(name = "last_modified_by", length = 36)
  @Size(max = 36)
  public String getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  /** Setter for <code>public.user_group.last_modified_by</code>. */
  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
}