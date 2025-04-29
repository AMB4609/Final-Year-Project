package com.lambdacode.creditscoreanalysiswebapplication.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CreditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Use LAZY fetching to avoid loading unnecessary data
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "pdf_data", columnDefinition = "LONGBLOB")
    private byte[] pdfData;  // Store PDF as a byte array, will be base64 encoded when needed

    @Column(name = "recommendation_requested")
    private boolean recommendationRequested;  // Whether a recommendation was requested by the client

    @Column(name = "recommendation_notes")
    private String recommendationNotes;  // Staff's personalized recommendation notes

    @Column(name = "staff_email")
    private String staffEmail;  // Staff who worked on the recommendation

    @Column(name = "status")
    private String status;  // Status of the report: 'pending', 'completed'

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public CreditReport() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getPdfData() {
        return pdfData;
    }

    public void setPdfData(byte[] pdfData) {
        this.pdfData = pdfData;
    }

    public boolean isRecommendationRequested() {
        return recommendationRequested;
    }

    public void setRecommendationRequested(boolean recommendationRequested) {
        this.recommendationRequested = recommendationRequested;
    }

    public String getRecommendationNotes() {
        return recommendationNotes;
    }

    public void setRecommendationNotes(String recommendationNotes) {
        this.recommendationNotes = recommendationNotes;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
