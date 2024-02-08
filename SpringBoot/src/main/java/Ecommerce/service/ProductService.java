package Ecommerce.service;

import java.util.List;

import Ecommerce.entity.Product;

public interface ProductService
{
    Product createProduct(Product product);
    Product getProductById(Long productId);
    List<Product> getAllProduct();
    Product updateProduct(Product product);
    void deleteProduct(Long productId);
}
