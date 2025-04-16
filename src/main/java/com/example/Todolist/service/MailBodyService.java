package com.example.Todolist.service;

import org.springframework.stereotype.Service;

import com.example.Todolist.domain.User;
import com.example.Todolist.domain.dto.MailBody;

@Service
public class MailBodyService {
    // private final OneTimePasswordService oneTimePasswordService;

    // public MailBodyService(OneTimePasswordService oneTimePasswordService) {
    // this.oneTimePasswordService = oneTimePasswordService;
    // }

    public MailBody handleCreatAMailBody(User user, String otp) {
        MailBody mailBody = new MailBody();
        mailBody.setTo(user.getEmail());
        mailBody.setSubject("OTP forgotPassword");
        mailBody.setText("Đây là OTP xác nhận của bạn: " + otp);
        return mailBody;
    }
}
