package Ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.entity.User;
import Ecommerce.repository.UserRepository;
import Ecommerce.service.UserService;

@Service
public class UserServiceImpl implements UserService 
{

    @Autowired
    public UserRepository userRepository;

    @Override
    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.get();
    }

    @Override
    public List<User> getAllUser()
    {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user)
    {
        User existingUser = userRepository.findById(user.getId()).get();
        existingUser.setFullName(user.getFullName());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        User updateUser = userRepository.save(existingUser);

        return updateUser;
    }

    @Override
    public void deleteUser(Long userId)
    {
        userRepository.deleteById(userId);
    }

    // Login
    @Override
    public Optional<User> authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            if (user.getPassword().equals(password))
            {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

       
}


 