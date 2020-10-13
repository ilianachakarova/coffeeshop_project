package com.chakarova.demo.web;

import com.chakarova.demo.model.service.DailyReportServiceModel;
import com.chakarova.demo.service.DailyReportService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/canvasjschart")
public class CanvasjsChartController {


    private final DailyReportService dailyReportService;
    private final Gson gson;
    @Autowired
    public CanvasjsChartController(DailyReportService dailyReportService, Gson gson) {
        this.dailyReportService = dailyReportService;
        this.gson = gson;
    }



    @GetMapping()
    @PreAuthorize("isAuthenticated()")
   public String getDataPoints(Model model){
        Gson gsonObj = new Gson();
        Map<Object,Object> map = null;
        List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

        List<DailyReportServiceModel>reports = this.dailyReportService.findAllReports();
        for (DailyReportServiceModel report : reports) {
            map = new HashMap<Object,Object>();
            map.put("x",report.getDate().getDayOfMonth());
            map.put("y", report.getRevenue());
            list.add(map);
        }

        String dataPoints = gsonObj.toJson(list);
        dataPoints = dataPoints.replace("\"", "");
        model.addAttribute("dataPoints",dataPoints);

        return "orders/chart";
    }
}
