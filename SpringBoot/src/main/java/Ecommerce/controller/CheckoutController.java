package Ecommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.dto.CheckoutDto;
import Ecommerce.entity.Cart;
import Ecommerce.entity.Checkout;
import Ecommerce.entity.User;
import Ecommerce.service.CheckoutService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CheckoutController {
    // checkout
    
    private final CheckoutService checkoutService;
    private final Map<String, User> loggedInUsers = new HashMap<>();
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
