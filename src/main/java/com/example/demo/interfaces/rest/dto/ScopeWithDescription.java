package com.example.demo.interfaces.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScopeWithDescription {
  private String scope;
  private String description;
}
