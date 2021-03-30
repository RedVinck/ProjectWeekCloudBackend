package com.redvinck.SpringBootProjectWeekCloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    public void save(Product product) {
            productRepository.save(product);
    }
    
    
}
