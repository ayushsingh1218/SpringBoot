package Ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.entity.Product;
import Ecommerce.repository.ProductRepository;
import Ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService
{

    @Autowired
    public ProductRepository productRepository;

    @Override
    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId)
    {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAllProduct()
    {
        return productRepository.findAll();        
    }

    @Override
    public Product updateProduct(Product product)
    {
        Product existingProduct = productRepository.findById(product.getId()).get();
        existingProduct.setProductName(product.getProductName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setAbout(product.getAbout());

        Product updateProduct = productRepository.save(existingProduct);
        return updateProduct;


    }

    @Override
    public void deleteProduct(Long productId)
    {
        productRepository.deleteById(productId);        
    }
    
}
