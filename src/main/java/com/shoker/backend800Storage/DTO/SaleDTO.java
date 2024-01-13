package com.shoker.backend800Storage.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.math.BigDecimal;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private LocalDateTime creationDate;
    private Long clientId;
    private Long sellerId;
    private Long productId;
    private int quantity;
    private BigDecimal price;

}
