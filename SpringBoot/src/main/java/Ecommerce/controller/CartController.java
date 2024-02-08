package Ecommerce.controller;


import java.util.List;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.dto.CartDto;
import Ecommerce.entity.Cart;
import Ecommerce.entity.User;
import Ecommerce.service.CartService;
import Ecommerce.service.SessionService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CartController {
    
    private final CartService cartService;
     private final SessionService sessionService;
     @PostMapping("/cart/{productId}")
    public ResponseEntity<Cart> addToCart(@RequestHeader("Authorization") String sessionId, @PathVariable Long productId, @RequestBody CartDto cartDto) {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user != null) {
            Cart cartItem = cartService.addToCart(user.getId(), productId, cartDto.getQuantity());
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Retrieve Cart
    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<Cart>> getCartDetails(@PathVariable Long userId) {
        List<Cart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    } 

    // Update Cart
    @PutMapping("/cart/{cartId}")
    public ResponseEntity<Cart> updateCart(@RequestHeader("Authorization") String sessionId, @PathVariable Long cartId, @RequestBody CartDto cartDto) {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user != null) {
            Cart updatedCartItem = cartService.updateCart(cartId, cartDto);
            return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Delete Cart Item
    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCartItem(@RequestHeader("Authorization") String sessionId, @PathVariable Long cartId) {
        User user = sessionService.loggedInUsers.get(sessionId);
        if (user != null) {
            cartService.deleteCartItem(cartId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

}
