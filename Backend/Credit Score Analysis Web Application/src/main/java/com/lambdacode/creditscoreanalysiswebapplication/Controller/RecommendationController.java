package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.itextpdf.text.DocumentException;
import com.lambdacode.creditscoreanalysiswebapplication.Request.AppendRecommendationRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.CreditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/credit-reports")
public class RecommendationController {

    @Autowired
    private CreditReportService creditReportService;

    // Endpoint to request a personalized recommendation (from the client)
    @PostMapping("/request/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JwtResponse> requestRecommendation(@PathVariable String userEmail, @RequestParam MultipartFile file) throws IOException {
        byte[] pdfData = file.getBytes();
        JwtResponse response = creditReportService.requestRecommendation(userEmail, pdfData);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/append/{id}")
    @PreAuthorize(" hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<JwtResponse> appendRecommendation(@PathVariable Long id, @RequestBody AppendRecommendationRequest request) throws DocumentException, IOException {
        JwtResponse response = creditReportService.appendRecommendation(id, request.getRecommendationNotes(), request.getUserEmail());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable Long id) {
        try {
            byte[] pdfData = creditReportService.getPDFById(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=credit_report.pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/staff/reports")
    @PreAuthorize(" hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<JwtResponse> getReportsForStaff() {
        try {
            JwtResponse response = creditReportService.getReportsForStaff();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/user/reports/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JwtResponse> getReportsForUser(@PathVariable String userEmail) {
        JwtResponse response = creditReportService.getReportsForUser(userEmail);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/storeWithoutRecommendation/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> storeReportWithoutRecommendation(
            @PathVariable String userEmail,
            @RequestParam("pdfFile") MultipartFile pdfFile) throws IOException {

        byte[] pdfData = pdfFile.getBytes();

        Object response = creditReportService.storeReportWithoutRecommendation(userEmail, pdfData);
        return ResponseEntity.ok(response);
    }
}
