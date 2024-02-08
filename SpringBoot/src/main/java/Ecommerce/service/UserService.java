package Ecommerce.service;

import java.util.List;
import java.util.Optional;

import Ecommerce.entity.User;

public interface UserService
{
    User createUser(User user);
    User getUserById(Long userId);
    List <User> getAllUser();
    User updateUser(User user);
    void deleteUser(Long userId);
    Optional<User> authenticate(String email, String password);
    
}
