package com.finance.finmailing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO implements MessageDTO{

    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "email")
    private String email;
}
