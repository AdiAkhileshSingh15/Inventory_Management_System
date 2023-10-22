package com.app.ecommerce.model;

import javax.management.relation.Role;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
        @Id
        private Long userID;
        @NotEmpty(message = "Please provide a username")
        private String userName;
        @NotEmpty(message = "Please provide a password")
        @Size(min = 8, max = 32, message = "Your password must be between 8 and 32 characters")
        private String password;
        @NotEmpty(message = "Please provide your full name")
        private String name;
        @Email
        private String email;
        private Long phone;
        @NotNull(message = "Please annotate a role")
        private Role role;

        public User(String userName, String password, String name, String email, Long phone, Role role) {
                super();
                this.userName = userName;
                this.password = password;
                this.name = name;
                this.email = email;
                this.phone = phone;
                this.role = role;
        }
}