package com.example.skillshare.DTO;


import lombok.Builder;

@Builder
public record MailBody(String To , String Subject, String Body) {

}
