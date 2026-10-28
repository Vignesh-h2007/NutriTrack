package com.nutrition.service;

import com.nutrition.dao.ReportDAO;
import com.nutrition.dao.ReportDAOImpl;
import com.nutrition.model.ReportData;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Service for building user progress reports.
 */
public class ReportService {

    private final ReportDAO reportDAO = new ReportDAOImpl();

    public ReportData getReport(int userId, String filterRange) throws SQLException {
        LocalDate today = LocalDate.now();
        Date startDate, endDate;
        endDate = Date.valueOf(today);

        switch (filterRange == null ? "week" : filterRange.toLowerCase()) {
            case "today":
                startDate = endDate;
                break;
            case "month":
                startDate = Date.valueOf(today.withDayOfMonth(1));
                break;
            case "custom":
                // Custom handled by caller who passes explicit dates
                startDate = Date.valueOf(today.minusDays(6));
                break;
            default: // week
                startDate = Date.valueOf(today.minusDays(6));
                break;
        }
        return reportDAO.generateUserReport(userId, filterRange, startDate, endDate);
    }

    public ReportData getCustomReport(int userId, String startStr, String endStr) throws SQLException {
        Date startDate = Date.valueOf(startStr);
        Date endDate = Date.valueOf(endStr);
        if (startDate.after(endDate)) throw new IllegalArgumentException("Start date must be before end date.");
        return reportDAO.generateUserReport(userId, "custom", startDate, endDate);
    }
}
