package Ecommerce.dto;

import lombok.Data;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UserDtoLogin
{
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;    
}
