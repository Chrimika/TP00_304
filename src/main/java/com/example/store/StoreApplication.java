package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Entity
    @Table(name = "products")
    public static class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private String name;
        
        @Column(nullable = false)
        private Double price;
        
        @Column(nullable = false)
        private Integer quantity;
        
        // Constructeurs
        public Product() {}
        
        public Product(String name, Double price, Integer quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        
        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public interface ProductRepository extends JpaRepository<Product, Long> {
        List<Product> findByNameContaining(String name);
    }

    @RestController
    @RequestMapping("/api/products")
    public static class ProductController {
        
        @Autowired
        private ProductRepository productRepository;
        
        @GetMapping
        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }
        
        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable Long id) {
            Optional<Product> product = productRepository.findById(id);
            return product.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        }
        
        @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody Product product) {
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        }
        
        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
            return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setQuantity(productDetails.getQuantity());
                    Product updatedProduct = productRepository.save(existingProduct);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
        }
        
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
    }
}