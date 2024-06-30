package com.mav.emailservice.controllers;

import com.mav.emailservice.entity.EmailData;
import com.mav.emailservice.service.EmailApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api-v1")
public class EmailController {

    @Autowired
    private EmailApiService emailApiService;

    @GetMapping("/read-emails")
    public String readEmails(@RequestParam String filePath) {
        return emailApiService.readEmailDataFromExcel(filePath);
    }
}