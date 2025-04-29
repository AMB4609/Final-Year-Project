package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationApprovalRequest(List<String> staffEmails, RegisterRequest registerRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(staffEmails.toArray(new String[0]));

        message.setSubject("User Registration Approval Needed: " + registerRequest.getUserName());

        String emailBody = String.format(
                "Dear Staff,\n\n" +
                        "A new user has requested to register in the system. Please review the details below:\n\n" +
                        "Name: %s\n" +
                        "Email: %s\n" +
                        "Role: %s\n\n" +
                        "To approve or reject this request, please click the following link:\n" +
                        "http://localhost:4209/api/register/pendingApprovalUserList\n\n" +
                        "Best Regards,\n Credit Score System Notification",
                registerRequest.getUserName(),
                registerRequest.getUserEmail(),
                registerRequest.getRole(),
                registerRequest.getUserEmail(),
                registerRequest.getUserEmail()
        );
        message.setText(emailBody);
        mailSender.send(message);
    }
}
