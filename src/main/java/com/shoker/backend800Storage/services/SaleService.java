package com.shoker.backend800Storage.services;

import com.shoker.backend800Storage.DTO.SaleDTO;
import com.shoker.backend800Storage.exeption.ResourceNotFoundException;
import com.shoker.backend800Storage.models.Client;
import com.shoker.backend800Storage.models.Product;
import com.shoker.backend800Storage.models.Sale;
import com.shoker.backend800Storage.repositories.ClientRepository;
import com.shoker.backend800Storage.repositories.ProductRepository;
import com.shoker.backend800Storage.repositories.SaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SaleService {
    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, ClientRepository clientRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    public List<SaleDTO> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public SaleDTO createSale(SaleDTO saleDTO) {
        log.info("Creating new sale");
        Sale sale = convertToEntity(saleDTO);
        if (sale.getClient() == null || sale.getProduct() == null) {
            throw new ResourceNotFoundException("Client or Product not found");
        }
        Sale savedSale = saleRepository.save(sale);
        return convertToDTO(savedSale);
    }

    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        log.info("Updating sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found for this id :: " + id));

        updateSaleEntityFromDTO(sale, saleDTO);
        Sale updatedSale = saleRepository.save(sale);
        return convertToDTO(updatedSale);
    }


    private SaleDTO convertToDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getId());
        saleDTO.setCreationDate(sale.getCreationDate());

        if (sale.getClient() != null) {
            saleDTO.setClientId(sale.getClient().getId());
        } else {
            log.warn("Sale with ID {} has no associated client", sale.getId());
        }

        saleDTO.setSellerId(sale.getSellerId());

        if (sale.getProduct() != null) {
            saleDTO.setProductId(sale.getProduct().getId());
        } else {
            log.warn("Sale with ID {} has no associated product", sale.getId());
        }

        saleDTO.setQuantity(sale.getQuantity());
        saleDTO.setPrice(sale.getPrice());
        return saleDTO;
    }


    private Sale convertToEntity(SaleDTO saleDTO) {
        Sale sale = new Sale();

        Client client = clientRepository.findById(saleDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for id: " + saleDTO.getClientId()));
        sale.setClient(client);

        Product product = productRepository.findById(saleDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id: " + saleDTO.getProductId()));
        sale.setProduct(product);

        sale.setSellerId(saleDTO.getSellerId());
        sale.setQuantity(saleDTO.getQuantity());
        sale.setPrice(saleDTO.getPrice());
        sale.setCreationDate(LocalDateTime.now());

        return sale;
    }


    private void updateSaleEntityFromDTO(Sale sale, SaleDTO saleDTO) {

        if (!sale.getClient().getId().equals(saleDTO.getClientId())) {
            Client newClient = clientRepository.findById(saleDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + saleDTO.getClientId()));
            sale.setClient(newClient);
        }
        if (!sale.getProduct().getId().equals(saleDTO.getProductId())) {
            Product newProduct = productRepository.findById(saleDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + saleDTO.getProductId()));
            sale.setProduct(newProduct);
        }
        sale.setQuantity(saleDTO.getQuantity());
        sale.setPrice(saleDTO.getPrice());
    }
    public SaleDTO getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found for this id :: " + id));
        return convertToDTO(sale);
    }


}
