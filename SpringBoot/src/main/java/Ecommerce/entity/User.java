package Ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_login")

public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String fullName;

    @Column(name = "user_mobile", nullable = false, unique = true)
    private Long mobileNumber;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    
}
