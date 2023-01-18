/*
 * This file is generated by jOOQ.
 */
package com.example.demo.infrastructure.repositories.tables.pojos;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.domain.Persistable;
import javax.persistence.Version;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Slf4j
@Data
@EntityListeners({AuditingEntityListener.class})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Entity
@Table(
    name = "oauth2_authorization_consent",
    schema = "public"
)
public class Oauth2AuthorizationConsent implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Column(name = "registered_client_id", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String registeredClientId;

    @Column(name = "principal_name", nullable = false, length = 200)
    @NotNull
    @Size(max = 200)
    private String principalName;

    @Column(name = "authorities", nullable = false, length = 1000)
    @NotNull
    @Size(max = 1000)
    private String authorities;


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Oauth2AuthorizationConsent other = (Oauth2AuthorizationConsent) obj;
        if (this.registeredClientId == null) {
            if (other.registeredClientId != null)
                return false;
        }
        else if (!this.registeredClientId.equals(other.registeredClientId))
            return false;
        if (this.principalName == null) {
            if (other.principalName != null)
                return false;
        }
        else if (!this.principalName.equals(other.principalName))
            return false;
        if (this.authorities == null) {
            if (other.authorities != null)
                return false;
        }
        else if (!this.authorities.equals(other.authorities))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.registeredClientId == null) ? 0 : this.registeredClientId.hashCode());
        result = prime * result + ((this.principalName == null) ? 0 : this.principalName.hashCode());
        result = prime * result + ((this.authorities == null) ? 0 : this.authorities.hashCode());
        return result;
    }
    
    @Override
    public String getId() {
        return this.registeredClientId;
    }
    
    @JsonIgnore
    @Override
    public boolean isNew() {
        return null == this.principalName;
    }
    
}