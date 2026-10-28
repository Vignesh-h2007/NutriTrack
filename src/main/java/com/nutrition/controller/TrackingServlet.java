package com.nutrition.controller;

import com.nutrition.model.WaterLog;
import com.nutrition.model.WeightHistory;
import com.nutrition.service.TrackingService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/track/*")
public class TrackingServlet extends HttpServlet {

    private final TrackingService trackingService = new TrackingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if ("/water/today".equals(path)) {
                int total = trackingService.getTodayWaterIntake(userId);
                List<WaterLog> logs = trackingService.getWaterLogsToday(userId);
                List<Map<String, Object>> logList = new ArrayList<>();
                for (WaterLog wl : logs) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("logId", wl.getLogId());
                    m.put("amountMl", wl.getAmountMl());
                    m.put("notes", wl.getNotes() != null ? wl.getNotes() : "");
                    m.put("loggedAt", wl.getLoggedAt() != null ? wl.getLoggedAt().toString() : "");
                    logList.add(m);
                }
                out.print(JsonUtil.success(Map.of("totalMl", total, "logs", logList)));
            } else if ("/weight/history".equals(path)) {
                int limit = 30;
                try { limit = Integer.parseInt(req.getParameter("limit")); } catch (Exception ignored) {}
                List<WeightHistory> history = trackingService.getWeightHistory(userId, limit);
                List<Map<String, Object>> histList = new ArrayList<>();
                for (WeightHistory wh : history) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("recordId", wh.getRecordId());
                    m.put("weightKg", wh.getWeightKg());
                    m.put("notes", wh.getNotes() != null ? wh.getNotes() : "");
                    m.put("recordedAt", wh.getRecordedAt() != null ? wh.getRecordedAt().toString() : "");
                    String dStr = wh.getRecordedAt() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(wh.getRecordedAt()) : "";
                    m.put("logDate", dStr);
                    histList.add(m);
                }
                out.print(JsonUtil.successList(histList));
            } else if ("/weight/latest".equals(path)) {
                WeightHistory wh = trackingService.getLatestWeight(userId);
                if (wh == null) { out.print(JsonUtil.success(Map.of("weightKg", 0))); return; }
                out.print(JsonUtil.success(Map.of("weightKg", wh.getWeightKg(), "recordedAt", wh.getRecordedAt() != null ? wh.getRecordedAt().toString() : "")));
            } else if ("/bmi".equals(path)) {
                double w = Double.parseDouble(req.getParameter("weightKg"));
                double h = Double.parseDouble(req.getParameter("heightCm"));
                double bmi = trackingService.calculateBMI(w, h);
                out.print(JsonUtil.success(Map.of("bmi", Math.round(bmi * 10.0) / 10.0, "category", trackingService.getBMICategory(bmi))));
            } else {
                resp.setStatus(404);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            Map<String, String> body = JsonUtil.parseBody(req);
            if ("/water".equals(path)) {
                int amount = Integer.parseInt(body.getOrDefault("amountMl", "250"));
                String notes = body.getOrDefault("notes", "");
                WaterLog log = trackingService.logWater(userId, amount, notes);
                out.print(JsonUtil.success(Map.of("logId", log.getLogId(), "message", "Water logged")));
            } else if ("/weight".equals(path)) {
                double weight = Double.parseDouble(body.getOrDefault("weightKg", "0"));
                String notes  = body.getOrDefault("notes", "");
                WeightHistory record = trackingService.logWeight(userId, weight, notes);
                out.print(JsonUtil.success(Map.of("recordId", record.getRecordId(), "message", "Weight logged")));
            } else {
                resp.setStatus(404);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            out.print(JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if (path != null && path.startsWith("/water/")) {
                int logId = Integer.parseInt(path.substring("/water/".length()));
                boolean ok = trackingService.deleteWaterLog(userId, logId);
                out.print(ok ? JsonUtil.success("Deleted") : JsonUtil.error("Delete failed"));
            } else {
                resp.setStatus(404);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) throw new SecurityException("Not authenticated");
        return (int) session.getAttribute("userId");
    }
}
