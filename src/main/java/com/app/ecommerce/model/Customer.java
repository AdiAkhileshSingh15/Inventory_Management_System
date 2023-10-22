package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Customer {

        @Id
        private Long customerID;
        @NotEmpty(message = "Customer name cannot be empty")
        private String customerName;
        @NotEmpty
        private String vehicle;
        @Email(message = "Please fill in a valid email address")
        private String email;
        @NotEmpty
        @Size(min = 10, max = 10, message = "Please fill in a valid contact number (10-digits)")
        private String contactno;

        public Customer(String customerName, String vehicle, String email, String contactno) {
                super();
                this.customerName = customerName;
                this.vehicle = vehicle;
                this.email = email;
                this.contactno = contactno;
        }
}