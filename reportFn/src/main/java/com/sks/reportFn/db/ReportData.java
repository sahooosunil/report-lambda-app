package com.sks.reportFn.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "report_data")
@Data
public class ReportData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double value;
}
