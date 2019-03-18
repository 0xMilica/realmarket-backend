package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.ReportsController;
import io.realmarket.propeler.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportsControllerImpl implements ReportsController {

  private final ReportService reportService;

  @Autowired
  public ReportsControllerImpl(ReportService reportService) {
    this.reportService = reportService;
  }
}
