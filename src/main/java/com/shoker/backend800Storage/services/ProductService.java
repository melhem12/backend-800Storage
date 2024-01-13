package com.shoker.backend800Storage.services;

import com.shoker.backend800Storage.DTO.ProductDTO;
import com.shoker.backend800Storage.exeption.ResourceNotFoundException;
import com.shoker.backend800Storage.models.Product;
import com.shoker.backend800Storage.repositories.ProductRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


    @Service
    @Transactional
    public class ProductService {
        private final Logger logger = LoggerFactory.getLogger(ProductService.class);
        private final ProductRepository productRepository;

        @Autowired
        public ProductService(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        public List<ProductDTO> getAllProducts() {
            logger.info("Fetching all products");
            return productRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        public ProductDTO createProduct(ProductDTO productDTO) {
            logger.info("Creating new product: {}", productDTO.getName());
            Product product = convertToEntity(productDTO);
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        }
        public ProductDTO getProductById(Long id) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
            return convertToDTO(product);
        }


        public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

            // Update entity
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCategory(productDTO.getCategory());
            // ... other fields

            logger.info("Updating product: {}", id);
            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }


        private ProductDTO convertToDTO(Product product) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategory(product.getCategory());
            productDTO.setCreationDate(product.getCreationDate());
            return productDTO;
        }

        private Product convertToEntity(ProductDTO productDTO) {
            Product product = new Product();
            if (productDTO.getId() != null) {
                product.setId(productDTO.getId());
            }
            product.setCreationDate(new Date());
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCategory(productDTO.getCategory());

            return product;
        }


}
