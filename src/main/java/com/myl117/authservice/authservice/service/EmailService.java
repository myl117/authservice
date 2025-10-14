package com.myl117.authservice.authservice.service;

import javax.management.RuntimeErrorException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendEmail(String to, String subject, String text) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setFrom("myl117meta@gmail.com");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text, true);

    mailSender.send(message);
  }

  public void sendVerificationEmail(String name, String toEmail, String token) {
    try {
    String verificationLink = "http://localhost:3008/api/auth/verify?token=" + token;

    String subject = "Verify your email";
    String body = "<p>Hi " + name + " ,</p>"
                + "<p>Thank you for signing up! Please verify your email by clicking the link below:</p>"
                + "<p><a href='" + verificationLink + "'>Verify Email</a></p>"
                + "<br><p>If you did not sign up, please ignore this email.</p>";

    sendEmail(toEmail, subject, body);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email");
    }
  }

  public void sendPasswordResetEmail(String toEmail, String token) {
    try {
    String passwordResetLink = "http://localhost:3008/api/auth/changepassword?token=" + token;

    String subject = "Change your password";
    String body = "<p>Hi</p>"
                + "<p>Please reset your password by clicking the link below:</p>"
                + "<p><a href='" + passwordResetLink + "'>Reset Password</a></p>"
                + "<br><p>If you did not sign up, please ignore this email.</p>";

    sendEmail(toEmail, subject, body);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email");
    }
  }
}
