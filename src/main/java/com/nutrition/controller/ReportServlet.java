package com.nutrition.controller;

import com.nutrition.model.ReportData;
import com.nutrition.service.ReportService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/report/*")
public class ReportServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            ReportData report;
            if ("/custom".equals(path)) {
                String start = req.getParameter("startDate");
                String end   = req.getParameter("endDate");
                if (start == null || end == null) { resp.setStatus(400); out.print(JsonUtil.error("startDate and endDate required")); return; }
                report = reportService.getCustomReport(userId, start, end);
            } else {
                String range = req.getParameter("range");
                if (range == null) range = path != null && path.length() > 1 ? path.substring(1) : "week";
                report = reportService.getReport(userId, range);
            }
            out.print(buildReportJson(report));
        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            out.print(JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    private String buildReportJson(ReportData r) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"success\":true,\"data\":{");
        sb.append("\"filterRange\":\"").append(r.getFilterRange() != null ? r.getFilterRange() : "").append("\",");
        sb.append("\"startDate\":\"").append(r.getStartDate() != null ? r.getStartDate() : "").append("\",");
        sb.append("\"endDate\":\"").append(r.getEndDate() != null ? r.getEndDate() : "").append("\",");
        sb.append("\"totalCaloriesConsumed\":").append(r.getTotalCaloriesConsumed()).append(",");
        sb.append("\"totalCaloriesBurned\":").append(r.getTotalCaloriesBurned()).append(",");
        sb.append("\"totalProteinG\":").append(r.getTotalProteinG()).append(",");
        sb.append("\"totalCarbsG\":").append(r.getTotalCarbsG()).append(",");
        sb.append("\"totalFatG\":").append(r.getTotalFatG()).append(",");
        sb.append("\"totalWaterMl\":").append(r.getTotalWaterMl()).append(",");
        sb.append("\"totalExerciseMinutes\":").append(r.getTotalExerciseMinutes()).append(",");
        sb.append("\"completedMealsCount\":").append(r.getCompletedMealsCount()).append(",");
        sb.append("\"completedExercisesCount\":").append(r.getCompletedExercisesCount()).append(",");
        sb.append("\"latestWeightKg\":").append(r.getLatestWeightKg()).append(",");
        // Date series
        sb.append("\"dateSeries\":[");
        if (r.getDateSeries() != null) {
            for (int i = 0; i < r.getDateSeries().size(); i++) {
                ReportData.DailyMetric d = r.getDateSeries().get(i);
                if (i > 0) sb.append(",");
                sb.append("{");
                sb.append("\"date\":\"").append(d.getDate()).append("\",");
                sb.append("\"caloriesConsumed\":").append(d.getCaloriesConsumed()).append(",");
                sb.append("\"caloriesBurned\":").append(d.getCaloriesBurned()).append(",");
                sb.append("\"proteinG\":").append(d.getProteinG()).append(",");
                sb.append("\"carbsG\":").append(d.getCarbsG()).append(",");
                sb.append("\"fatG\":").append(d.getFatG()).append(",");
                sb.append("\"waterMl\":").append(d.getWaterMl()).append(",");
                sb.append("\"exerciseMinutes\":").append(d.getExerciseMinutes()).append(",");
                sb.append("\"mealsCompleted\":").append(d.getMealsCompleted()).append(",");
                sb.append("\"weightKg\":").append(d.getWeightKg());
                sb.append("}");
            }
        }
        sb.append("],");
        // Activity log
        sb.append("\"activityLog\":[");
        if (r.getActivityLog() != null) {
            for (int i = 0; i < r.getActivityLog().size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(r.getActivityLog().get(i).replace("\"", "'")).append("\"");
            }
        }
        sb.append("]}}");
        return sb.toString();
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) throw new SecurityException("Not authenticated");
        return (int) session.getAttribute("userId");
    }
}
