package Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecommerce.entity.Cart;
import Ecommerce.entity.Product;
import Ecommerce.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>
{
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findAllByUser(User user);
    List<Cart> findAllByUser_Id(Long userId);
}