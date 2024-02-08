package Ecommerce.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ecommerce.dto.CartDto;
import Ecommerce.dto.CheckoutDto;
import Ecommerce.dto.UserDtoLogin;
import Ecommerce.entity.Cart;
import Ecommerce.entity.Checkout;
import Ecommerce.entity.Product;
import Ecommerce.entity.User;
import Ecommerce.service.CartService;
import Ecommerce.service.CheckoutService;
import Ecommerce.service.ProductService;
import Ecommerce.service.UserService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserControllerr 
{

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final CheckoutService checkoutService;
    
        

    // private final Map<String, User> loggedInUsers = new HashMap<>();

    private final Map<String, User> loggedInUsers = new HashMap<>();

    // User Registration
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Retrieve User by ID
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Retrieve All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update User
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete User
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Successfully deleted!", HttpStatus.OK);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDtoLogin userDtoLogin) {
        Optional<User> optionalUser = userService.authenticate(userDtoLogin.getEmail(), userDtoLogin.getPassword());
        if (optionalUser.isPresent()) {
            String sessionId = generateSessionId();
            loggedInUsers.put(sessionId, optionalUser.get());
            return ResponseEntity.ok(sessionId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String sessionId) {
        loggedInUsers.remove(sessionId);
        return ResponseEntity.ok("Logged out successfully");
    }

    // Create Product
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Retrieve Product by ID
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // Retrieve All Products
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Update Product
    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {
        product.setId(productId);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // Delete Product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product successfully deleted!", HttpStatus.OK);
    }

    // Add to Cart
    @PostMapping("/cart/{productId}")
    public ResponseEntity<Cart> addToCart(@RequestHeader("Authorization") String sessionId, @PathVariable Long productId, @RequestBody CartDto cartDto) {
        User user = loggedInUsers.get(sessionId);
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
        User user = loggedInUsers.get(sessionId);
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
        User user = loggedInUsers.get(sessionId);
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

   
// checkout
   @PostMapping("/checkout")
public ResponseEntity<?> checkout(@RequestHeader("Authorization") String sessionId, @RequestBody CheckoutDto checkoutDto) {
    User user = loggedInUsers.get(sessionId);
    if (user != null) {
        Checkout checkout = checkoutService.checkout(user.getId(), checkoutDto);
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("billId", checkout.getBillId());
        response.put("userName", user.getFullName());
        response.put("userMobileNumber", user.getMobileNumber());
        response.put("deliveryAddress", checkout.getDeliveryAddress());
        List<Map<String, Object>> cartDetails = new ArrayList<>();
        for (Cart cartItem : checkout.getCartItems()) {
            Map<String, Object> cartItemDetails = new HashMap<>();
            cartItemDetails.put("productName", cartItem.getProduct().getProductName());
            cartItemDetails.put("quantity", cartItem.getQuantity());
            cartItemDetails.put("price", cartItem.getTotalPrice());
            cartDetails.add(cartItemDetails);
        }
        response.put("cartDetails", cartDetails);
        double totalAmount = checkout.getCartItems().stream().mapToDouble(Cart::getTotalPrice).sum();
        response.put("totalAmount", totalAmount);
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
    }
}


    

}
           



