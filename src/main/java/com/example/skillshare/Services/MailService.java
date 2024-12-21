package com.example.skillshare.Services;

import com.example.skillshare.DTO.MailBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.tools.JavaFileManager;

@Service
public class MailService {


    private JavaMailSender javaMailSender;


    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.To());
        message.setFrom("sunkeadarsh3@gmail.com");
        message.setSubject(mailBody.Subject());
        message.setText(mailBody.Body());
        javaMailSender.send(message);
    }
}
