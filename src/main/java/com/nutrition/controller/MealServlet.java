package com.nutrition.controller;

import com.nutrition.model.MealLog;
import com.nutrition.model.MealPlan;
import com.nutrition.service.MealService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.util.*;

@WebServlet("/api/meal/*")
public class MealServlet extends HttpServlet {

    private final MealService mealService = new MealService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if ("/plans".equals(path)) {
                String dateParam = req.getParameter("date");
                if (dateParam != null && !dateParam.isEmpty()) {
                    List<MealPlan> plans = mealService.getMealPlansForDate(userId, Date.valueOf(dateParam));
                    out.print(buildPlanListJson(plans));
                } else {
                    List<MealPlan> plans = mealService.getAllMealPlans(userId);
                    out.print(buildPlanListJson(plans));
                }
            } else if ("/logs".equals(path)) {
                String dateParam = req.getParameter("date");
                Date date = dateParam != null && !dateParam.isEmpty() ? Date.valueOf(dateParam) : new Date(System.currentTimeMillis());
                List<MealLog> logs = mealService.getMealLogsForDate(userId, date);
                out.print(buildLogListJson(logs));
            } else if ("/calorie-target".equals(path)) {
                String g = req.getParameter("gender");
                double h = Double.parseDouble(req.getParameter("heightCm"));
                double w = Double.parseDouble(req.getParameter("weightKg"));
                int a    = Integer.parseInt(req.getParameter("age"));
                String al = req.getParameter("activityLevel");
                int target = mealService.computeDailyCalorieTarget(w, h, a, g, al);
                out.print(JsonUtil.success(Map.of("targetCalories", target)));
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
            if ("/plans".equals(path)) {
                String planName = body.get("planName");
                Date planDate   = Date.valueOf(body.getOrDefault("planDate", new Date(System.currentTimeMillis()).toString()));
                String foodIdsStr   = body.getOrDefault("foodIds", "");
                String quantitiesStr = body.getOrDefault("quantities", "");
                String mealTypesStr = body.getOrDefault("mealTypes", "");
                List<Integer> foodIds   = parseIntList(foodIdsStr);
                List<Double> quantities = parseDoubleList(quantitiesStr);
                List<String> mealTypes  = parseStringList(mealTypesStr);
                MealPlan plan = mealService.createMealPlan(userId, planName, planDate, foodIds, quantities, mealTypes);
                out.print(JsonUtil.success(Map.of("planId", plan.getPlanId(), "message", "Meal plan created")));
            } else if ("/logs".equals(path)) {
                int foodId    = Integer.parseInt(body.getOrDefault("foodId", "0"));
                double qty    = Double.parseDouble(body.getOrDefault("quantityGrams", "100"));
                String mType  = body.getOrDefault("mealType", "BREAKFAST");
                Date logDate  = Date.valueOf(body.getOrDefault("logDate", new Date(System.currentTimeMillis()).toString()));
                int planItemId = Integer.parseInt(body.getOrDefault("planItemId", "0"));
                MealLog log = mealService.logMeal(userId, planItemId, foodId, qty, mType, logDate);
                out.print(JsonUtil.success(Map.of("logId", log.getLogId(), "message", "Meal logged")));
            } else if (path != null && path.matches("/logs/\\d+/complete")) {
                int logId = Integer.parseInt(path.split("/")[2]);
                boolean ok = mealService.completeMealLog(userId, logId);
                out.print(ok ? JsonUtil.success("Meal marked as completed") : JsonUtil.error("Failed to complete meal"));
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if (path != null && path.matches("/logs/\\d+/complete")) {
                int logId = Integer.parseInt(path.split("/")[2]);
                boolean ok = mealService.completeMealLog(userId, logId);
                out.print(ok ? JsonUtil.success("Meal marked as completed") : JsonUtil.error("Failed to complete meal"));
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if (path != null && path.startsWith("/plans/")) {
                int planId = Integer.parseInt(path.substring("/plans/".length()));
                boolean ok = mealService.deleteMealPlan(userId, planId);
                out.print(ok ? JsonUtil.success("Plan deleted") : JsonUtil.error("Delete failed"));
            } else if (path != null && path.startsWith("/logs/")) {
                int logId = Integer.parseInt(path.substring("/logs/".length()));
                boolean ok = mealService.deleteMealLog(userId, logId);
                out.print(ok ? JsonUtil.success("Log deleted") : JsonUtil.error("Delete failed"));
            } else {
                resp.setStatus(404);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    // ── Helpers ──

    private String buildPlanListJson(List<MealPlan> plans) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MealPlan p : plans) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("planId", p.getPlanId()); m.put("planName", p.getPlanName());
            m.put("planDate", p.getPlanDate() != null ? p.getPlanDate().toString() : "");
            m.put("totalCalories", p.getTotalCalories()); m.put("items", p.getItems());
            list.add(m);
        }
        return JsonUtil.successList(list);
    }

    private String buildLogListJson(List<MealLog> logs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MealLog l : logs) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("logId", l.getLogId()); m.put("foodId", l.getFoodId());
            m.put("foodName", l.getFoodName() != null ? l.getFoodName() : "");
            m.put("mealType", l.getMealType()); m.put("quantityGrams", l.getQuantityGrams());
            m.put("calories", l.getCalories()); m.put("protein", l.getProtein());
            m.put("carbs", l.getCarbs()); m.put("fat", l.getFat());
            m.put("status", l.getStatus() != null ? l.getStatus() : "PENDING");
            m.put("loggedDate", l.getLoggedDate() != null ? l.getLoggedDate().toString() : "");
            list.add(m);
        }
        return JsonUtil.successList(list);
    }

    private List<Integer> parseIntList(String csv) {
        List<Integer> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) { try { result.add(Integer.parseInt(s.trim())); } catch (NumberFormatException ignored) {} }
        return result;
    }

    private List<Double> parseDoubleList(String csv) {
        List<Double> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) { try { result.add(Double.parseDouble(s.trim())); } catch (NumberFormatException ignored) {} }
        return result;
    }

    private List<String> parseStringList(String csv) {
        List<String> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) result.add(s.trim());
        return result;
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) throw new SecurityException("Not authenticated");
        return (int) session.getAttribute("userId");
    }
}
