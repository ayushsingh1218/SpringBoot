package Ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.entity.Product;
import Ecommerce.entity.User;
import Ecommerce.service.ProductService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProductController {
    
     private final ProductService productService;
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

}
