package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Brand {
        @Id
        private Long brandId;
        private String brandName;

        public Brand(String brandName) {
                this.brandName = brandName;
        }
}
