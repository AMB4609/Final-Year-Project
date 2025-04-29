package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.itextpdf.text.DocumentException;
import com.lambdacode.creditscoreanalysiswebapplication.Model.CreditReport;
import com.lambdacode.creditscoreanalysiswebapplication.Request.AppendRecommendationRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;

import java.io.IOException;

public interface CreditReportService {

    JwtResponse requestRecommendation(String userEmail, byte[] pdfData);

    JwtResponse appendRecommendation(Long id, String recommendationNotes, String StaffEmail) throws IOException, DocumentException;

    JwtResponse storeReportWithoutRecommendation(String userEmail, byte[] pdfData);

    JwtResponse getReportsForStaff();

    JwtResponse getReportsForUser(String userEmail);

    CreditReport getById(Long id);

    byte[] getPDFById(Long id);
}
