package com.lambdacode.creditscoreanalysiswebapplication.Model;

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
    private Long staffId;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_password")
    @JsonIgnore
    private String staffPassword;

    @Column(name = "staff_phone")
    private Long staffPhone;

    @Column(name = "staff_email")
    private String staffEmail;

    @Column(name = "staff_address")
    private String staffAddress;

    @Column(name = "role")
    private String role;

    private boolean status;

    private LocalDate approveDate;
    private LocalDate registrationDate;
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;


    // Getter and Setter for userId
    public Long getUserId() {
        return staffId;
    }

    public void setUserId(Long userId) {
        this.staffId = userId;
    }

    // Getter and Setter for staffName
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    // Getter and Setter for staffPassword
    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    // Getter and Setter for staffPhone
    public Long getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(Long staffPhone) {
        this.staffPhone = staffPhone;
    }

    // Getter and Setter for staffEmail
    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    // Getter and Setter for staffAddress
    public String getStaffAddress() {
        return staffAddress;
    }

    public void setStaffAddress(String staffAddress) {
        this.staffAddress = staffAddress;
    }

    // Getter and Setter for position
    public String getRole() {
        return role;
    }

    public void setRole(String position) {
        this.role = position;
    }

    // Getter and Setter for status
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter and Setter for approveDate
    public LocalDate getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(LocalDate approveDate) {
        this.approveDate = approveDate;
    }

    // Getter and Setter for registrationDate
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void setProfilePicture(byte[] bytes) {
        this.profilePicture = bytes;
    }
    public byte[] getProfilePicture() { return profilePicture; }
}
