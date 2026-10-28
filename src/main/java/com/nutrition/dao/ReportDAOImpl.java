package com.nutrition.dao;

import com.nutrition.model.ReportData;
import com.nutrition.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public ReportData generateUserReport(int userId, String filterRange, Date startDate, Date endDate) throws SQLException {
        ReportData report = new ReportData();
        report.setFilterRange(filterRange);
        report.setStartDate(startDate.toString());
        report.setEndDate(endDate.toString());

        try (Connection conn = DBConnection.getConnection()) {
            // Meal aggregates (only completed meals)
            String mealAggSql = "SELECT SUM(calories) AS total_cal, SUM(protein) AS total_protein, " +
                    "SUM(carbs) AS total_carbs, SUM(fat) AS total_fat, COUNT(log_id) AS total_meals " +
                    "FROM meal_logs WHERE user_id = ? AND logged_date BETWEEN ? AND ? AND (status = 'COMPLETED' OR status IS NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(mealAggSql)) {
                stmt.setInt(1, userId); stmt.setDate(2, startDate); stmt.setDate(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        report.setTotalCaloriesConsumed(rs.getInt("total_cal"));
                        report.setTotalProteinG(rs.getDouble("total_protein"));
                        report.setTotalCarbsG(rs.getDouble("total_carbs"));
                        report.setTotalFatG(rs.getDouble("total_fat"));
                        report.setCompletedMealsCount(rs.getInt("total_meals"));
                    }
                }
            }

            // Water aggregates
            String waterAggSql = "SELECT SUM(amount_ml) FROM water_logs WHERE user_id = ? AND (DATE(logged_at) BETWEEN ? AND ? OR logged_date BETWEEN ? AND ?)";
            try (PreparedStatement stmt = conn.prepareStatement(waterAggSql)) {
                stmt.setInt(1, userId); stmt.setDate(2, startDate); stmt.setDate(3, endDate);
                stmt.setDate(4, startDate); stmt.setDate(5, endDate);
                try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) report.setTotalWaterMl(rs.getInt(1)); }
            }

            // Exercise aggregates from completions & sessions
            int totalExSec = 0;
            int totalExCal = 0;
            int totalExCnt = 0;

            String compAggSql = "SELECT SUM(duration_seconds) AS dur, SUM(calories_burned) AS cal, COUNT(completion_id) AS cnt " +
                    "FROM exercise_completions WHERE user_id = ? AND completion_date BETWEEN ? AND ?";
            try (PreparedStatement stmt = conn.prepareStatement(compAggSql)) {
                stmt.setInt(1, userId); stmt.setDate(2, startDate); stmt.setDate(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        totalExSec += rs.getInt("dur");
                        totalExCal += rs.getInt("cal");
                        totalExCnt += rs.getInt("cnt");
                    }
                }
            }

            String exAggSql = "SELECT SUM(actual_duration) AS dur, SUM(actual_calories) AS cal, COUNT(session_id) AS cnt " +
                    "FROM exercise_sessions WHERE user_id = ? AND session_date BETWEEN ? AND ? AND status = 'COMPLETED'";
            try (PreparedStatement stmt = conn.prepareStatement(exAggSql)) {
                stmt.setInt(1, userId); stmt.setDate(2, startDate); stmt.setDate(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        totalExSec += rs.getInt("dur");
                        totalExCal += rs.getInt("cal");
                        totalExCnt += rs.getInt("cnt");
                    }
                }
            }
            report.setTotalExerciseMinutes(totalExSec / 60);
            report.setTotalCaloriesBurned(totalExCal);
            report.setCompletedExercisesCount(totalExCnt);

            // Latest weight
            String weightSql = "SELECT weight_kg FROM weight_history WHERE user_id = ? AND (DATE(recorded_at) <= ? OR record_date <= ?) ORDER BY recorded_at DESC, weight_history_id DESC LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(weightSql)) {
                stmt.setInt(1, userId); stmt.setDate(2, endDate); stmt.setDate(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) report.setLatestWeightKg(rs.getDouble(1)); }
            }

            // Build date series
            Map<String, ReportData.DailyMetric> metricMap = new HashMap<>();
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate cur = start;
            while (!cur.isAfter(end)) { String d = cur.format(fmt); metricMap.put(d, new ReportData.DailyMetric(d)); cur = cur.plusDays(1); }

            // Daily meals
            String dm = "SELECT logged_date, SUM(calories) c, SUM(protein) p, SUM(carbs) cb, SUM(fat) f, COUNT(log_id) cnt FROM meal_logs WHERE user_id=? AND logged_date BETWEEN ? AND ? AND (status = 'COMPLETED' OR status IS NULL) GROUP BY logged_date";
            try (PreparedStatement s = conn.prepareStatement(dm)) {
                s.setInt(1,userId); s.setDate(2,startDate); s.setDate(3,endDate);
                try (ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        ReportData.DailyMetric m = metricMap.get(r.getDate("logged_date").toString());
                        if (m!=null) { m.setCaloriesConsumed(r.getInt("c")); m.setProteinG(r.getDouble("p")); m.setCarbsG(r.getDouble("cb")); m.setFatG(r.getDouble("f")); m.setMealsCompleted(r.getInt("cnt")); }
                    }
                }
            }
            // Daily water
            String dw = "SELECT COALESCE(logged_date, DATE(logged_at)) ld, SUM(amount_ml) wml FROM water_logs WHERE user_id=? AND (logged_date BETWEEN ? AND ? OR DATE(logged_at) BETWEEN ? AND ?) GROUP BY COALESCE(logged_date, DATE(logged_at))";
            try (PreparedStatement s = conn.prepareStatement(dw)) {
                s.setInt(1,userId); s.setDate(2,startDate); s.setDate(3,endDate);
                s.setDate(4,startDate); s.setDate(5,endDate);
                try (ResultSet r = s.executeQuery()) {
                    while(r.next()) {
                        java.sql.Date d = r.getDate("ld");
                        if (d != null) {
                            ReportData.DailyMetric m=metricMap.get(d.toString());
                            if(m!=null) m.setWaterMl(r.getInt("wml"));
                        }
                    }
                }
            }
            // Daily exercise completions
            String dcomp = "SELECT completion_date, SUM(duration_seconds) dur, SUM(calories_burned) cal FROM exercise_completions WHERE user_id=? AND completion_date BETWEEN ? AND ? GROUP BY completion_date";
            try (PreparedStatement s = conn.prepareStatement(dcomp)) {
                s.setInt(1,userId); s.setDate(2,startDate); s.setDate(3,endDate);
                try (ResultSet r = s.executeQuery()) {
                    while(r.next()) {
                        java.sql.Date d = r.getDate("completion_date");
                        if (d != null) {
                            ReportData.DailyMetric m=metricMap.get(d.toString());
                            if(m!=null) {
                                m.setExerciseMinutes(m.getExerciseMinutes() + (r.getInt("dur")/60));
                                m.setCaloriesBurned(m.getCaloriesBurned() + r.getInt("cal"));
                            }
                        }
                    }
                }
            }
            // Daily exercise sessions
            String de = "SELECT session_date, SUM(actual_duration) dur, SUM(actual_calories) cal FROM exercise_sessions WHERE user_id=? AND session_date BETWEEN ? AND ? AND status='COMPLETED' GROUP BY session_date";
            try (PreparedStatement s = conn.prepareStatement(de)) {
                s.setInt(1,userId); s.setDate(2,startDate); s.setDate(3,endDate);
                try (ResultSet r = s.executeQuery()) {
                    while(r.next()) {
                        java.sql.Date d = r.getDate("session_date");
                        if (d != null) {
                            ReportData.DailyMetric m=metricMap.get(d.toString());
                            if(m!=null) {
                                m.setExerciseMinutes(m.getExerciseMinutes() + (r.getInt("dur")/60));
                                m.setCaloriesBurned(m.getCaloriesBurned() + r.getInt("cal"));
                            }
                        }
                    }
                }
            }
            // Daily weight
            String dwt = "SELECT COALESCE(record_date, DATE(recorded_at)) rd, weight_kg FROM weight_history WHERE user_id=? AND (record_date BETWEEN ? AND ? OR DATE(recorded_at) BETWEEN ? AND ?) ORDER BY recorded_at ASC, weight_history_id ASC";
            try (PreparedStatement s = conn.prepareStatement(dwt)) {
                s.setInt(1,userId); s.setDate(2,startDate); s.setDate(3,endDate);
                s.setDate(4,startDate); s.setDate(5,endDate);
                try (ResultSet r = s.executeQuery()) {
                    while(r.next()) {
                        java.sql.Date d = r.getDate("rd");
                        if (d != null) {
                            ReportData.DailyMetric m=metricMap.get(d.toString());
                            if(m!=null) m.setWeightKg(r.getDouble("weight_kg"));
                        }
                    }
                }
            }

            List<ReportData.DailyMetric> series = new ArrayList<>();
            cur = start;
            while (!cur.isAfter(end)) { series.add(metricMap.get(cur.format(fmt))); cur = cur.plusDays(1); }
            report.setDateSeries(series);

            // Activity log
            List<String> acts = new ArrayList<>();
            String actSql = "SELECT action, created_at FROM audit_logs WHERE user_id=? ORDER BY created_at DESC LIMIT 10";
            try (PreparedStatement s = conn.prepareStatement(actSql)) {
                s.setInt(1, userId);
                try (ResultSet r = s.executeQuery()) { while(r.next()) acts.add(r.getTimestamp("created_at") + " - " + r.getString("action")); }
            }
            report.setActivityLog(acts);
        }
        return report;
    }
}
