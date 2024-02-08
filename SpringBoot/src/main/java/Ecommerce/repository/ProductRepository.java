package Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>
{

}
