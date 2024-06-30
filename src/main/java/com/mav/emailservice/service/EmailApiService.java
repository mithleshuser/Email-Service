package com.mav.emailservice.service;

import com.mav.emailservice.utill.MXRecordChecker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

@Service
public class EmailApiService {

    @Autowired
    private EmailSenderService emailSenderService;

    public String readEmailDataFromExcel(String filePath) {
        Workbook workbook = null;

        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Add a header for the status column if it doesn't exist
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("Header row is null");
                return "Header row is null";
            }
            Cell statusHeaderCell = headerRow.getCell(3);
            if (statusHeaderCell == null) {
                statusHeaderCell = headerRow.createCell(3);
                statusHeaderCell.setCellValue("Status");
            } else {
                statusHeaderCell.setCellValue("Status");
            }

            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                String recipient = getCellValueAsString(row.getCell(0));
                String subject = getCellValueAsString(row.getCell(1));
                String body = getCellValueAsString(row.getCell(2));
                Cell statusCell = row.getCell(3); // Status cell

                if (statusCell == null) {
                    statusCell = row.createCell(3); // Create status cell if it does not exist
                }

                try {
                    if (!isrRcipientValid(recipient)) {
                        throw new AddressException("Invalid email address");
                    }
                    emailSenderService.sendHtmlEmail(recipient, subject, body);

                    statusCell.setCellValue("Success");
                    System.out.println("Email sent successfully to " + recipient);
                } catch (AddressException e) {
                    statusCell.setCellValue("Fail - Invalid Email Address");
                    System.out.println("Failed to send email to " + recipient + ": Invalid email address");
                } catch (Exception e) {
                    statusCell.setCellValue("Fail - General Exception");
                    System.out.println("Failed to send email to " + recipient + ": " + e.getMessage());
                }

            }
            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "Email sent successfully Please check the status in Excel sheet";
    }
    // Full email validation method
    public boolean isrRcipientValid(String email) {
        String domain = getDomainFromEmail(email);
        return isValidEmailFormat(email) &&
                isDomainValid(domain) &&
                MXRecordChecker.hasMXRecords(domain)&&
                isValidEmailDomain(email);
    }
    public boolean isValidEmailDomain(String email) {
        String domainRegex = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(domainRegex);
        return pattern.matcher(email).matches();
    }
    public static boolean isDomainValid(String domain) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            return addresses.length > 0;
        } catch (UnknownHostException e) {
            return false;
        }
    }
    // Method to validate email format
    public boolean isValidEmailFormat(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }
    // Method to get the domain from an email address
    private static String getDomainFromEmail(String email) {
        return email.substring(email.indexOf("@") + 1);
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


}