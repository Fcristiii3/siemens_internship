package com.siemens.internship.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @NotBlank(message = "Name is required")
    private String name;
    @Column
    private String description;
    @Column
    private String status;
    @Column
    @NotBlank(message = "Email is required")
    @Email(message = "invalid email format")
    private String email;
}