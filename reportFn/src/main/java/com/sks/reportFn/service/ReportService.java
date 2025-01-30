package com.sks.reportFn.service;

import com.sks.reportFn.db.ReportData;
import com.sks.reportFn.db.ReportDataRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportDataRepository repository;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SesClient sesClient;

    public List<ReportData> fetchData() {
        return repository.findAll();
    }

    public File generateReport(List<ReportData> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Value");

        int rowNum = 1;
        for (ReportData record : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getId());
            row.createCell(1).setCellValue(record.getName());
            row.createCell(2).setCellValue(record.getValue());
        }

        File file = File.createTempFile("report", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
        return file;
    }

    public String uploadToS3(File file) {
        String fileName = "reports/" + file.getName();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(fileName)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        return "https://s3.amazonaws.com/" + s3Bucket + "/" + fileName;
    }

    public void sendEmail(String reportUrl) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses("recipient@example.com").build())
                .message(Message.builder()
                        .subject(Content.builder().data("Daily Report").build())
                        .body(Body.builder().text(Content.builder().data("Report URL: " + reportUrl).build()).build())
                        .build())
                .source("your-email@example.com")
                .build();
        sesClient.sendEmail(request);
    }
}
