package Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecommerce.entity.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long>
{
    
}