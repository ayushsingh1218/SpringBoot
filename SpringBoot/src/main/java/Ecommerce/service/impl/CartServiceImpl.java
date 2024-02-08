package Ecommerce.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.dto.CartDto;
import Ecommerce.entity.Cart;
import Ecommerce.entity.Product;
import Ecommerce.entity.User;
import Ecommerce.repository.CartRepository;
import Ecommerce.repository.ProductRepository;
import Ecommerce.repository.UserRepository;
import Ecommerce.service.CartService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

@Service
public class CartServiceImpl implements CartService
{

    @Autowired
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        Cart existingCart = cartRepository.findByUserAndProduct(user, product);
        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.setTotalPrice(existingCart.getTotalPrice() + (product.getPrice() * quantity));
            return cartRepository.save(existingCart);
        } else {
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setTotalPrice(product.getPrice() * quantity);
            return cartRepository.save(newCartItem);
        }
    }
    
    @Override
    public Cart updateCart(Long cartId, CartDto cartDto) {
        Cart cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));
        
        Product product = cartItem.getProduct();
        int oldQuantity = cartItem.getQuantity();
        int newQuantity = cartDto.getQuantity();
        
        // Update the quantity
        cartItem.setQuantity(newQuantity);
        
        // Check if the product price has changed
        if (product.getPrice() != cartDto.getProductPrice()) {
            // Update the product price
            product.setPrice(cartDto.getProductPrice());
            
            // Recalculate the total price based on the new product price
            int newTotalPrice = cartDto.getProductPrice() * newQuantity;
            cartItem.setTotalPrice(newTotalPrice);
            
            // Log the changes
            System.out.println("Product price updated. New total price: " + newTotalPrice);
        }
        
        return cartRepository.save(cartItem);
    }
    
    @Override
    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }
    
    @Override
    public List<Cart> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        
        return cartRepository.findAllByUser(user);
    }

    @Override
    public int getTotalCartAmount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        List<Cart> cartItems = cartRepository.findAllByUser(user);

        return cartItems.stream().mapToInt(cart -> cart.getTotalPrice()).sum();
    }

   
}
