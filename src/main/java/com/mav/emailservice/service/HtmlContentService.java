package com.mav.emailservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HtmlContentService {

    public String generateHtmlContent(Map<String,String> cotan) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html>" +
                           "<body><table border='1'>");
        htmlContent.append("<tr>");
        htmlContent.append("<td>");
                                    cotan.get("smartcard");
        htmlContent.append("</td>");
        htmlContent.append("</tr>");
        htmlContent.append("</table></body></html>");
        return htmlContent.toString();
    }
}