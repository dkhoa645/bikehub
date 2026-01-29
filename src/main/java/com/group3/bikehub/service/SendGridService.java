package com.group3.bikehub.service;

import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendGridService {

    private static final String EMAIL_ENDPOINT = "mail/send";
    private final SendGrid sendGrid;
    private final Email fromEmail;

    public void dispatchEmail(String emailId, String subject, String body) throws IOException {
        Email toEmail = new Email(emailId);

        Content content = new Content("text/html", body);

        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint(EMAIL_ENDPOINT);
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        if (response.getStatusCode() >= 400) {
            log.error("SendGrid error: {}", response.getBody());
            throw new AppException(ErrorCode.SEND_EMAIL_FAILED);
        }
    }
}
