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

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            if ("/register".equals(path)) {
                handleRegister(req, resp, out);
            } else if ("/login".equals(path)) {
                handleLogin(req, resp, out);
            } else if ("/logout".equals(path)) {
                handleLogout(req, resp, out);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(JsonUtil.error("Server error: " + e.getMessage()));
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        Map<String, String> body = JsonUtil.parseBody(req);
        String name           = body.get("name");
        String email          = body.get("email");
        String password       = body.get("password");
        String gender         = body.getOrDefault("gender", "Other");
        int age               = Integer.parseInt(body.getOrDefault("age", "0"));
        double height         = Double.parseDouble(body.getOrDefault("heightCm", "0"));
        double weight         = Double.parseDouble(body.getOrDefault("weightKg", "0"));
        String activityLevel  = body.getOrDefault("activityLevel", "sedentary");
        String healthGoal     = body.getOrDefault("healthGoal", "MAINTENANCE");

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

        User user = userService.register(name, email, password, gender, age, height, weight, activityLevel, healthGoal, medicalConditionIds);
        HttpSession session = req.getSession(true);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());

        out.print(JsonUtil.success(Map.of(
            "userId", user.getUserId(),
            "name", user.getName(),
            "email", user.getEmail()
        )));
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        Map<String, String> body = JsonUtil.parseBody(req);
        String email    = body.get("email");
        String password = body.get("password");

        User user = userService.login(email, password);
        HttpSession session = req.getSession(true);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setMaxInactiveInterval(3600 * 8); // 8 hours

        out.print(JsonUtil.success(Map.of(
            "userId", user.getUserId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "healthGoal", user.getHealthGoal() != null ? user.getHealthGoal() : ""
        )));
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        out.print(JsonUtil.success("Logged out successfully"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if ("/me".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(JsonUtil.error("Not authenticated"));
                return;
            }
            int userId = (int) session.getAttribute("userId");
            try {
                User user = userService.getUserById(userId);
                out.print(JsonUtil.success(Map.of(
                    "userId", user.getUserId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "healthGoal", user.getHealthGoal() != null ? user.getHealthGoal() : "",
                    "activityLevel", user.getActivityLevel() != null ? user.getActivityLevel() : "",
                    "age", user.getAge(),
                    "heightCm", user.getHeightCm(),
                    "weightKg", user.getWeightKg(),
                    "gender", user.getGender() != null ? user.getGender() : ""
                )));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(JsonUtil.error("Server error: " + e.getMessage()));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print(JsonUtil.error("Endpoint not found"));
        }
    }
}
