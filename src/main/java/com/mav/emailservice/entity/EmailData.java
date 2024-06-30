package com.mav.emailservice.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;


@Data
@Builder
@Table(name = "EMAILDATA")
@Entity
@NoArgsConstructor
public class EmailData implements Serializable {
    @Id
    private String recipient;
    @Column
    private String subject;
    @Column
    private String body;


    public EmailData(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmailData{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}