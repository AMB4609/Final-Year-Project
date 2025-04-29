package com.lambdacode.creditscoreanalysiswebapplication.Service.CreditReportServiceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Model.CreditReport;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.ActivityRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.CreditReportRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Response.CreditReportResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.CreditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CreditReportServiceImpl implements CreditReportService {

    @Autowired
    private CreditReportRepository creditReportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    Activity activity = new Activity();
    @Override
    public JwtResponse requestRecommendation(String userEmail, byte[] pdfData) {
        User user = userRepository.findByUserEmail(userEmail);
        CreditReport creditReport = new CreditReport();
        creditReport.setUser(user);
        creditReport.setPdfData(pdfData);
        creditReport.setRecommendationRequested(true);  // Client has requested a recommendation
        creditReport.setRecommendationNotes(null);     // No recommendation yet
        creditReport.setStatus("pending");
        creditReportRepository.save(creditReport);
        activity.setActivityDescription(user.getUserName() + " Requested for Personalized Recommendation ");
        activity.setActivityTime(LocalDateTime.now());
        activity.setUser(user);
        activityRepository.save(activity);
        return setResponseFields(creditReport, 200, "Recommendation request created.", true);
    }

    @Override
    public JwtResponse appendRecommendation(Long id, String recommendationNotes,String StaffEmail) throws IOException, DocumentException {
        CreditReport creditReport = creditReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        JwtResponse updatedPdfResponse = appendRecommendationToPDF(creditReport.getPdfData(), recommendationNotes);
        byte[] updatedPdfData = (byte[]) updatedPdfResponse.getData();

        creditReport.setPdfData(updatedPdfData);
        creditReport.setRecommendationNotes(recommendationNotes);
        creditReport.setRecommendationRequested(false);
        creditReport.setStatus("completed");

        creditReportRepository.save(creditReport);
        Activity activity = new Activity();
        activity.setActivityDescription(StaffEmail + " Appended Recommendation for " + creditReport.getUser().getUserEmail());
        activity.setActivityTime(LocalDateTime.now());
        activityRepository.save(activity);
        return setResponseFields(creditReport, 200, "Recommendation appended successfully.", true);
    }
    @Override
    public JwtResponse storeReportWithoutRecommendation(String userEmail, byte[] pdfData) {
        User user = userRepository.findByUserEmail(userEmail);
        CreditReport creditReport = new CreditReport();
        creditReport.setUser(user);
        creditReport.setPdfData(pdfData);
        creditReport.setRecommendationRequested(false);
        creditReport.setRecommendationNotes(null);
        creditReport.setStatus("completed");
        activity.setActivityDescription(" Stored CreditReport  without recommendation for " + user.getUserName());
        activity.setActivityTime(LocalDateTime.now());
        activity.setUser(user);
        activityRepository.save(activity);
        creditReportRepository.save(creditReport);

        return setResponseFields(creditReport, 200, "Report stored without recommendation.", true);
    }

    public JwtResponse getReportsForStaff() {
        try {
            System.out.println("Fetching reports for staff...");

            List<CreditReport> creditReports = creditReportRepository.findAll();

            if (creditReports.isEmpty()) {
                throw new RuntimeException("No CreditReports found");
            }

            List<CreditReportResponse> creditReportResponses = new ArrayList<>();

            for (CreditReport creditReport : creditReports) {
                if (creditReport.getRecommendationNotes() == null && creditReport.isRecommendationRequested()){
                    CreditReportResponse creditReportResponse = new CreditReportResponse();

                    creditReportResponse.setId(creditReport.getId());
                    creditReportResponse.setRecommendationNotes(creditReport.getRecommendationNotes());
                    creditReportResponse.setStatus(creditReport.getStatus());
                    creditReportResponse.setRecommendationRequested(creditReport.isRecommendationRequested());
                    creditReportResponse.setUserEmail(creditReport.getUser().getUserEmail());

                    creditReportResponse.setUserEmail(creditReport.getUser().getUserEmail());

                    creditReportResponses.add(creditReportResponse);
                }
            }

            return setResponseFields(creditReportResponses, 200, "Fetched reports for staff.", true);

        } catch (Exception e) {
            System.out.println("Error while fetching reports for staff: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error while fetching reports for staff", e);
        }
    }




    @Override
    public JwtResponse getReportsForUser(String userEmail) {
        try {
            System.out.println("Fetching reports for user...");
            User user = userRepository.findByUserEmail(userEmail);

            List<CreditReport> creditReports = creditReportRepository.findByUser(user);

            List<CreditReportResponse> creditReportResponses = new ArrayList<>();

            for (CreditReport creditReport : creditReports) {
                if (creditReport.getRecommendationNotes() != null) {
                    CreditReportResponse creditReportResponse = new CreditReportResponse();

                    creditReportResponse.setId(creditReport.getId());
                    creditReportResponse.setRecommendationNotes(creditReport.getRecommendationNotes());
                    creditReportResponse.setStatus(creditReport.getStatus());
                    creditReportResponse.setRecommendationRequested(creditReport.isRecommendationRequested());
                    creditReportResponse.setUserEmail(creditReport.getUser().getUserEmail());

                    creditReportResponses.add(creditReportResponse);
                }

            }
            return setResponseFields(creditReportResponses, 200, "Fetched reports for user.", true);

        } catch (Exception e) {
            System.out.println("Error while fetching reports for user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error while fetching reports for user", e);
        }
    }


    @Override
    public CreditReport getById(Long id) {
        return creditReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit report not found for ID: " + id));
    }

    @Override
    public byte[] getPDFById(Long id) {
        CreditReport creditReport = creditReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return creditReport.getPdfData();
    }


    private JwtResponse appendRecommendationToPDF(byte[] originalPdfData, String recommendationNotes) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfReader reader = new PdfReader(originalPdfData);
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        writer.getDirectContent().addTemplate(page, 0, 0);

        document.newPage();
        document.add(new Paragraph("Staff Review:"));
        document.add(new Paragraph(recommendationNotes));
        document.close();
        return setResponseFields(outputStream.toByteArray(), 200, "PDF updated with recommendation.", true);
    }

    private JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }
}
