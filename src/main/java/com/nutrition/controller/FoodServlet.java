package com.nutrition.controller;

import com.nutrition.model.Food;
import com.nutrition.service.FoodRecommendationService;
import com.nutrition.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/food/*")
public class FoodServlet extends HttpServlet {

    private final FoodRecommendationService foodService = new FoodRecommendationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        int userId = getSessionUserId(req);
        String path = req.getPathInfo();

        try {
            if ("/recommend".equals(path)) {
                List<Food> foods = foodService.getRecommendations(userId);
                out.print(buildFoodListJson(foods));
            } else if ("/search".equals(path)) {
                String query    = req.getParameter("q");
                String category = req.getParameter("category");
                List<Food> foods = foodService.searchFoods(query, category);
                out.print(buildFoodListJson(foods));
            } else if (path != null && path.matches("/\\d+")) {
                int foodId = Integer.parseInt(path.substring(1));
                Food f = foodService.getFoodById(foodId);
                if (f == null) { resp.setStatus(404); out.print(JsonUtil.error("Food not found")); return; }
                out.print(JsonUtil.success(foodToMap(f)));
            } else {
                List<Food> foods = foodService.getAllFoods();
                out.print(buildFoodListJson(foods));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print(JsonUtil.error(e.getMessage()));
        }
    }

    private String buildFoodListJson(List<Food> foods) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Food f : foods) list.add(foodToMap(f));
        return JsonUtil.successList(list);
    }

    private Map<String, Object> foodToMap(Food f) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("foodId", f.getFoodId());
        m.put("name", f.getName());
        m.put("category", f.getCategory() != null ? f.getCategory() : "");
        m.put("calories", f.getCaloriesPer100g());
        m.put("protein", f.getProteinG());
        m.put("carbs", f.getCarbsG());
        m.put("fat", f.getFatG());
        m.put("fiber", f.getFiberG());
        m.put("servingSize", f.getServingSizeG());
        m.put("description", f.getDescription() != null ? f.getDescription() : "");
        return m;
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) throw new SecurityException("Not authenticated");
        return (int) session.getAttribute("userId");
    }
}
