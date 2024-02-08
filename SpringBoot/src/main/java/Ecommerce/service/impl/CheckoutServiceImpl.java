package Ecommerce.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import Ecommerce.dto.CheckoutDto;
import Ecommerce.entity.Cart;
import Ecommerce.entity.Checkout;
import Ecommerce.entity.User;
import Ecommerce.repository.CheckoutRepository;
import Ecommerce.repository.UserRepository;
import Ecommerce.service.CartService;
import Ecommerce.service.CheckoutService;




@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final CheckoutRepository checkoutRepository;

    public CheckoutServiceImpl(UserRepository userRepository, CartService cartService, CheckoutRepository checkoutRepository) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public Checkout checkout(Long userId, CheckoutDto checkoutDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Validate if user has an active cart
        List<Cart> cartItems = cartService.getCartItems(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("User does not have any items in the cart.");
        }

        // Proceed with checkout
        Checkout checkout = new Checkout();
        checkout.setUser(user);
        checkout.setDeliveryAddress(checkoutDto.getDeliveryAddress());
        checkout.setBillId(generateBillId());

        List<Cart> cartItemList = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Cart item = new Cart();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            item.setCheckout(checkout);
            cartItemList.add(item);
        }

        checkout.setCartItems(cartItemList);

        // Clear user's cart after checkout
        // cartService.clearCart(userId);

        return checkoutRepository.save(checkout);
    }

    private String generateBillId() {
        // You can implement your own logic for generating a unique bill ID here
        return UUID.randomUUID().toString();
    }
}   
