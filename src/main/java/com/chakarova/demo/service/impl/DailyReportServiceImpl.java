package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.DailyReportRepository;
import com.chakarova.demo.model.entity.DailyReport;
import com.chakarova.demo.model.service.DailyReportServiceModel;
import com.chakarova.demo.service.DailyReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyReportServiceImpl implements DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public DailyReportServiceImpl(DailyReportRepository dailyReportRepository, ModelMapper modelMapper) {
        this.dailyReportRepository = dailyReportRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DailyReportServiceModel saveDailyReport(DailyReportServiceModel dailyReportServiceModel) {
        DailyReport dailyReport = this.dailyReportRepository.save
                (this.modelMapper.map(dailyReportServiceModel,DailyReport.class));
        return this.modelMapper.map(dailyReport,DailyReportServiceModel.class);

    }

    @Override
    public DailyReportServiceModel findReportByDate(LocalDate date) {
        return this.modelMapper.map(this.dailyReportRepository.findByDate(date),DailyReportServiceModel.class);
    }

    @Override
    public List<DailyReportServiceModel> findReportsFromDate(LocalDate date) {
        return this.dailyReportRepository.findAllByDateAfter(date).stream()
                .map(dr->this.modelMapper.map(dr,DailyReportServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyReportServiceModel> findAllReports() {
        return this.dailyReportRepository.findAll().stream()
                .map(dr->this.modelMapper.map(dr,DailyReportServiceModel.class))
                .collect(Collectors.toList());
    }
}
