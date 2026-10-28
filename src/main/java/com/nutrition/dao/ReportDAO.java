package com.nutrition.dao;

import com.nutrition.model.ReportData;
import java.sql.Date;
import java.sql.SQLException;

public interface ReportDAO {
    ReportData generateUserReport(int userId, String filterRange, Date startDate, Date endDate) throws SQLException;
}
