package com.chakarova.demo.service;

import com.chakarova.demo.dao.DailyReportRepository;
import com.chakarova.demo.model.entity.DailyReport;
import com.chakarova.demo.model.service.DailyReportServiceModel;
import com.chakarova.demo.service.impl.DailyReportServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DailyReportServiceTests {
   @Autowired
    private DailyReportRepository dailyReportRepository;
   private ModelMapper modelMapper;
   @Before
   public void init(){
       modelMapper = new ModelMapper();
   }

   @Test
    public void dailyReportService_saveDailyRecord_returnsCorrectValue(){
        DailyReportService dailyReportServiceToTest =
                new DailyReportServiceImpl(dailyReportRepository,modelMapper);

       DailyReport testDailyReport = new DailyReport();
       testDailyReport.setDate(LocalDate.of(2020,7,28));
       testDailyReport.setOrdersTotal(50);
       testDailyReport.setRevenue(BigDecimal.valueOf(4500));
       testDailyReport.setId(1L);

       dailyReportRepository.save(testDailyReport);

       Assert.assertEquals(1,dailyReportRepository.count());
    }

    @Test
    public void dailyReportService_findAll_returnsCorrectValue(){
        DailyReportService dailyReportServiceToTest =
                new DailyReportServiceImpl(dailyReportRepository,modelMapper);

        DailyReport testDailyReport = new DailyReport();
        testDailyReport.setDate(LocalDate.of(2020,7,28));
        testDailyReport.setOrdersTotal(50);
        testDailyReport.setRevenue(BigDecimal.valueOf(4500));
        testDailyReport.setId(1L);

        dailyReportRepository.save(testDailyReport);
       List<DailyReportServiceModel>reports = dailyReportServiceToTest.findAllReports();

        Assert.assertEquals(1,reports.size());
    }

    @Test
    public void dailyReportService_findAllFromDate_returnsCorrectValue(){
        DailyReportService dailyReportServiceToTest =
                new DailyReportServiceImpl(dailyReportRepository,modelMapper);

        DailyReport testDailyReport = new DailyReport();
        testDailyReport.setDate(LocalDate.of(2020,7,28));
        testDailyReport.setOrdersTotal(50);
        testDailyReport.setRevenue(BigDecimal.valueOf(4500));
        testDailyReport.setId(1L);

        dailyReportRepository.save(testDailyReport);
        List<DailyReportServiceModel>reports =
                dailyReportServiceToTest.findReportsFromDate(LocalDate.of(2020,7,27));

        Assert.assertEquals(1,reports.size());
    }

    @Test
    public void dailyReportService_findDailyRecordByDate_returnsCorrectValue(){
        DailyReportService dailyReportServiceToTest =
                new DailyReportServiceImpl(dailyReportRepository,modelMapper);

        DailyReport testDailyReport = new DailyReport();
        testDailyReport.setDate(LocalDate.of(2020,7,28));
        testDailyReport.setOrdersTotal(50);
        testDailyReport.setRevenue(BigDecimal.valueOf(4500));
        testDailyReport.setId(1L);

        dailyReportRepository.save(testDailyReport);
        dailyReportServiceToTest.findReportByDate(LocalDate.of(2020,7,28));

        Assert.assertEquals(1,dailyReportRepository.count());
    }

    @Test(expected = Exception.class)
    public void dailyReportService_findDailyRecordByDate_shouldThrowWithInvalidData(){
        DailyReportService dailyReportServiceToTest =
                new DailyReportServiceImpl(dailyReportRepository,modelMapper);

        DailyReport testDailyReport = new DailyReport();
        testDailyReport.setDate(LocalDate.of(1994,10,10));
        testDailyReport.setRevenue(BigDecimal.valueOf(500));
        testDailyReport.setOrdersTotal(50);
        dailyReportServiceToTest.saveDailyReport(this.modelMapper.map(testDailyReport,DailyReportServiceModel.class));
        dailyReportServiceToTest.findReportByDate(LocalDate.of(2021,12,12));
    }


}
