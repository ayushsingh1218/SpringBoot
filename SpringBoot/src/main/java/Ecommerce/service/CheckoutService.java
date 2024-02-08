package Ecommerce.service;

import Ecommerce.dto.CheckoutDto;
import Ecommerce.entity.Checkout;

public interface CheckoutService
{
    Checkout checkout(Long userId, CheckoutDto checkoutDto);
}