package com.lambdacode.librarymanagementsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Entity(name = "Staff")
public class Staff {
    @Id
    @GeneratedValue
    @Column(name = "staff_id")
    private Long Id;



    @Column(name = "staff_name")
    private String name;
    @Column(name = "staff_password")
    @JsonIgnore
    private String password;
    @Column(name = "staff_phone")
    private Long    phone;
    @Column(name = "staff_email")
    private String email;
    @Column(name = "staff_address")
    private String address;
    @Column(name = "position")
    private String position;
    private boolean status;

    private LocalDate approveDate;
    private LocalDate registrationDate;
}
