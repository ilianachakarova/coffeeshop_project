package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport,Long> {
    DailyReport findByDate(LocalDate date);
    List<DailyReport> findAllByDateAfter(LocalDate date);
}
