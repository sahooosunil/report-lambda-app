package com.sks.reportFn.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sks.reportFn.db.ReportData;
import com.sks.reportFn.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ReportLambdaHandler implements RequestHandler<Object, String> {

    @Autowired
    private ReportService reportService;

    @Override
    public String handleRequest(Object o, Context context) {
        context.getLogger().log("Lambda invoked with input: " + o);
        try {
            // 1. Fetch Data from RDS
            List<ReportData> data = reportService.fetchData();

            // 2. Generate Report
            File reportFile = reportService.generateReport(data);

            // 3. Upload to S3
            String s3Url = reportService.uploadToS3(reportFile);

            // 4. Send Email
            reportService.sendEmail(s3Url);
            return "Report generated and sent successfully!";
        } catch (IOException e) {
            context.getLogger().log("Error: " + e.getMessage());
            return "Failed to generate report";
        }
    }
}
