package com.nutrition.controller;

import com.nutrition.model.User;
import com.nutrition.service.UserService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.LinkedHashMap;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);

        try {
            User user = userService.getUserById(userId);
            if (user == null) { resp.setStatus(404); out.print(JsonUtil.error("User not found")); return; }
            
            java.util.List<Integer> condIds = new java.util.ArrayList<>();
            if (user.getMedicalConditions() != null) {
                for (com.nutrition.model.MedicalCondition mc : user.getMedicalConditions()) {
                    condIds.add(mc.getConditionId());
                }
            }

            Map<String, Object> userData = new LinkedHashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("gender", user.getGender() != null ? user.getGender() : "");
            userData.put("age", user.getAge());
            userData.put("heightCm", user.getHeightCm());
            userData.put("weightKg", user.getWeightKg());
            userData.put("activityLevel", user.getActivityLevel() != null ? user.getActivityLevel() : "");
            userData.put("healthGoal", user.getHealthGoal() != null ? user.getHealthGoal() : "");
            userData.put("dailyWaterTargetMl", user.getDailyWaterTargetMl() > 0 ? user.getDailyWaterTargetMl() : 100000);
            userData.put("medicalConditions", condIds);
            userData.put("hasMedicalCondition", !condIds.isEmpty());
            userData.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");

            out.print(JsonUtil.success(userData));
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
            Map<String, String> body = JsonUtil.parseBody(req);
            if ("/password".equals(path)) {
                boolean ok = userService.changePassword(userId,
                        body.get("oldPassword"), body.get("newPassword"));
                if (ok) out.print(JsonUtil.success("Password changed successfully"));
                else { resp.setStatus(400); out.print(JsonUtil.error("Password change failed")); }
            } else {
                java.util.List<Integer> medicalConditionIds = new java.util.ArrayList<>();
                String condStr = body.get("medicalConditions");
                if (condStr != null && !condStr.trim().isEmpty()) {
                    for (String s : condStr.split(",")) {
                        try {
                            String clean = s.replaceAll("[\\[\\]\"\\s]", "");
                            if (!clean.isEmpty()) medicalConditionIds.add(Integer.parseInt(clean));
                        } catch (NumberFormatException ignored) {}
                    }
                }

                int dailyWaterTarget = 0;
                try {
                    if (body.containsKey("dailyWaterTargetMl")) {
                        dailyWaterTarget = Integer.parseInt(body.get("dailyWaterTargetMl"));
                    }
                } catch (Exception ignored) {}

                boolean ok = userService.updateProfile(userId,
                        body.getOrDefault("name", ""),
                        Integer.parseInt(body.getOrDefault("age", "0")),
                        Double.parseDouble(body.getOrDefault("heightCm", "0")),
                        Double.parseDouble(body.getOrDefault("weightKg", "0")),
                        body.getOrDefault("activityLevel", ""),
                        body.getOrDefault("healthGoal", ""),
                        dailyWaterTarget,
                        medicalConditionIds);
                if (ok) out.print(JsonUtil.success("Profile updated successfully"));
                else { resp.setStatus(400); out.print(JsonUtil.error("Update failed")); }
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            out.print(JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SecurityException("Not authenticated");
        }
        return (int) session.getAttribute("userId");
    }
}
