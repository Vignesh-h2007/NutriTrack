package com.nutrition.controller;

import com.nutrition.model.Exercise;
import com.nutrition.model.ExerciseSession;
import com.nutrition.model.WorkoutPlan;
import com.nutrition.service.WorkoutService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.util.*;

@WebServlet("/api/workout/*")
public class WorkoutServlet extends HttpServlet {

    private final WorkoutService workoutService = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if ("/plans".equals(path) || path == null || "/".equals(path)) {
                List<WorkoutPlan> plans = workoutService.getPlans();
                out.print(JsonUtil.successList(plans));
            } else if ("/exercises".equals(path)) {
                String q = req.getParameter("q");
                String muscle = req.getParameter("muscle");
                List<Exercise> list = (q != null || muscle != null)
                        ? workoutService.searchExercises(q, muscle)
                        : workoutService.getAllExercises();
                out.print(buildExerciseListJson(list));
            } else if ("/recommend".equals(path)) {
                List<Exercise> list = workoutService.getRecommendedExercises(userId);
                out.print(buildExerciseListJson(list));
            } else if ("/sessions".equals(path)) {
                String dateParam = req.getParameter("date");
                if (dateParam != null && !dateParam.isEmpty()) {
                    List<ExerciseSession> sessions = workoutService.getSessionsByDate(userId, Date.valueOf(dateParam));
                    out.print(buildSessionListJson(sessions));
                } else {
                    List<ExerciseSession> sessions = workoutService.getRecentSessions(userId, 20);
                    out.print(buildSessionListJson(sessions));
                }
            } else if ("/history".equals(path)) {
                String dateParam = req.getParameter("date");
                if (dateParam != null && !dateParam.isEmpty()) {
                    List<com.nutrition.model.ExerciseCompletion> comps = workoutService.getCompletionsByDate(userId, Date.valueOf(dateParam));
                    out.print(buildCompletionListJson(comps));
                } else {
                    List<com.nutrition.model.ExerciseCompletion> comps = workoutService.getRecentCompletions(userId, 30);
                    out.print(buildCompletionListJson(comps));
                }
            } else if ("/calorie-estimate".equals(path)) {
                double met = Double.parseDouble(req.getParameter("met"));
                double weight = Double.parseDouble(req.getParameter("weightKg"));
                int dur = Integer.parseInt(req.getParameter("durationSeconds"));
                int cal = workoutService.estimateCaloriesBurned(met, weight, dur);
                out.print(JsonUtil.success(Map.of("estimatedCalories", cal)));
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
                String title = body.getOrDefault("title", "Custom Plan");
                String desc = body.getOrDefault("description", "");
                String goal = body.getOrDefault("targetGoal", "FITNESS");
                int weeks = Integer.parseInt(body.getOrDefault("durationWeeks", "4"));
                int cals = Integer.parseInt(body.getOrDefault("estimatedCaloriesBurn", "350"));

                WorkoutPlan plan = new WorkoutPlan(title, desc, goal, weeks, cals);
                WorkoutPlan created = workoutService.createPlan(plan);
                out.print(JsonUtil.success(created));
            } else if ("/complete-exercise".equals(path)) {
                int exId = Integer.parseInt(body.getOrDefault("exerciseId", "1"));
                String exName = body.getOrDefault("exerciseName", "Exercise");
                int dur = Integer.parseInt(body.getOrDefault("durationSeconds", "60"));
                int sets = Integer.parseInt(body.getOrDefault("setsCompleted", "3"));
                int reps = Integer.parseInt(body.getOrDefault("repetitionsCompleted", "10"));
                int cals = Integer.parseInt(body.getOrDefault("caloriesBurned", "30"));
                int score = Integer.parseInt(body.getOrDefault("dailyScore", "50"));
                int pct = Integer.parseInt(body.getOrDefault("completionPercentage", "100"));
                String dateStr = body.getOrDefault("completionDate", new Date(System.currentTimeMillis()).toString());

                com.nutrition.model.ExerciseCompletion comp = new com.nutrition.model.ExerciseCompletion(
                        userId, exId, exName, Date.valueOf(dateStr), dur, sets, reps, cals, pct, score);
                int compId = workoutService.logExerciseCompletion(comp);
                out.print(JsonUtil.success(Map.of("completionId", compId, "message", "Exercise completion recorded! 🏆", "score", score)));
            } else if ("/sessions".equals(path)) {
                Date sessionDate = Date.valueOf(body.getOrDefault("sessionDate", new Date(System.currentTimeMillis()).toString()));
                List<Integer> exerciseIds = parseIntList(body.getOrDefault("exerciseIds", ""));
                List<Integer> durations = parseIntList(body.getOrDefault("plannedDurations", ""));
                ExerciseSession s = workoutService.createSession(userId, sessionDate, exerciseIds, durations);
                out.print(JsonUtil.success(Map.of("sessionId", s.getSessionId(), "status", s.getStatus(), "message", "Session created")));
            } else if ("/log".equals(path)) {
                out.print(JsonUtil.success(Map.of("message", "Exercise set logged")));
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            Map<String, String> body = JsonUtil.parseBody(req);
            if ("/sessions".equals(path)) {
                int sessionId = Integer.parseInt(body.getOrDefault("sessionId", "0"));
                int actualDur = Integer.parseInt(body.getOrDefault("actualDurationSec", "1800"));
                int actualCal = Integer.parseInt(body.getOrDefault("actualCalories", "350"));
                boolean ok = workoutService.completeSession(userId, sessionId, actualDur, actualCal);
                out.print(ok ? JsonUtil.success("Session completed") : JsonUtil.error("Could not complete session"));
            } else {
                resp.setStatus(404);
                out.print(JsonUtil.error("Endpoint not found"));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    private String buildExerciseListJson(List<Exercise> list) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Exercise e : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("exerciseId", e.getExerciseId());
            m.put("name", e.getName());
            m.put("exerciseName", e.getName());
            m.put("category", e.getCategory() != null ? e.getCategory() : "");
            m.put("targetMuscle", e.getTargetMuscle() != null ? e.getTargetMuscle() : "");
            m.put("muscleGroup", e.getMuscleGroup() != null ? e.getMuscleGroup() : "");
            m.put("difficulty", e.getDifficulty() != null ? e.getDifficulty() : "");
            m.put("duration", e.getDuration());
            m.put("durationSeconds", e.getDurationSeconds());
            m.put("sets", e.getSets());
            m.put("repetitions", e.getRepetitions());
            m.put("caloriesBurn", e.getCaloriesBurn());
            m.put("estimatedCalories", e.getEstimatedCalories());
            m.put("instructions", e.getInstructions() != null ? e.getInstructions() : "");
            m.put("healthTags", e.getHealthTags() != null ? e.getHealthTags() : "");
            m.put("healthRuleStatus", e.getHealthRuleStatus() != null ? e.getHealthRuleStatus() : "ALLOWED");
            m.put("healthRuleReason", e.getHealthRuleReason() != null ? e.getHealthRuleReason() : "");
            out.add(m);
        }
        return JsonUtil.successList(out);
    }

    private String buildCompletionListJson(List<com.nutrition.model.ExerciseCompletion> list) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (com.nutrition.model.ExerciseCompletion c : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("completionId", c.getCompletionId());
            m.put("exerciseId", c.getExerciseId());
            m.put("exerciseName", c.getExerciseName());
            m.put("completionDate", c.getCompletionDate() != null ? c.getCompletionDate().toString() : "");
            m.put("durationSeconds", c.getDurationSeconds());
            m.put("durationMinutes", c.getDurationMinutes());
            m.put("setsCompleted", c.getSetsCompleted());
            m.put("repetitionsCompleted", c.getRepetitionsCompleted());
            m.put("caloriesBurned", c.getCaloriesBurned());
            m.put("completionPercentage", c.getCompletionPercentage());
            m.put("dailyScore", c.getDailyScore());
            out.add(m);
        }
        return JsonUtil.successList(out);
    }

    private String buildSessionListJson(List<ExerciseSession> sessions) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (ExerciseSession s : sessions) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("sessionId", s.getSessionId());
            m.put("sessionDate", s.getSessionDate() != null ? s.getSessionDate().toString() : "");
            m.put("status", s.getStatus() != null ? s.getStatus() : "");
            m.put("plannedDurationMin", s.getPlannedDurationMin());
            m.put("actualDurationMin", s.getActualDurationMin());
            m.put("actualCalories", s.getActualCalories());
            out.add(m);
        }
        return JsonUtil.successList(out);
    }

    private List<Integer> parseIntList(String csv) {
        List<Integer> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) { try { result.add(Integer.parseInt(s.trim())); } catch (NumberFormatException ignored) {} }
        return result;
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) return 1;
        return (int) session.getAttribute("userId");
    }
}
