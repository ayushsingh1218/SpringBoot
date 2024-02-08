package Ecommerce.service;

import java.util.List;

import Ecommerce.dto.CartDto;
import Ecommerce.entity.Cart;

public interface CartService
{
    Cart addToCart(Long userId, Long productId, int quantity);
    Cart updateCart(Long cartId, CartDto cartDto);
    void deleteCartItem(Long cartId);
    List<Cart> getCartItems(Long userId);

    int getTotalCartAmount(Long userId);

}
