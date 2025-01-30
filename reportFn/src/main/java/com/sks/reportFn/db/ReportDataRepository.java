package com.sks.reportFn.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {
}
