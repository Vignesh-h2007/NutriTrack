package com.nutrition.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class JsonUtil {

    public static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return "\"" + escapeJson((String) obj) + "\"";
        if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
        if (obj instanceof Date) return "\"" + obj.toString() + "\"";

        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                sb.append(toJson(list.get(i)));
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            int count = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (count > 0) sb.append(",");
                sb.append("\"").append(escapeJson(String.valueOf(entry.getKey()))).append("\":");
                sb.append(toJson(entry.getValue()));
                count++;
            }
            sb.append("}");
            return sb.toString();
        }

        // Specific Domain Entities Reflection / Serialization
        StringBuilder sb = new StringBuilder("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        int count = 0;
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            f.setAccessible(true);
            try {
                Object val = f.get(obj);
                if (count > 0) sb.append(",");
                sb.append("\"").append(f.getName()).append("\":");
                sb.append(toJson(val));
                count++;
            } catch (Exception ignored) {}
        }
        sb.append("}");
        return sb.toString();
    }

    public static String success(Object data) {
        if (data instanceof String) {
            return "{\"success\":true,\"message\":\"" + escapeJson((String) data) + "\"}";
        }
        return "{\"success\":true,\"data\":" + toJson(data) + "}";
    }

    public static String successList(List<?> list) {
        return "{\"success\":true,\"data\":" + toJson(list) + "}";
    }

    public static String error(String message) {
        return "{\"success\":false,\"error\":\"" + escapeJson(message) + "\",\"message\":\"" + escapeJson(message) + "\"}";
    }

    public static Map<String, String> parseBody(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString().trim();
            if (body.startsWith("{") && body.endsWith("}")) {
                body = body.substring(1, body.length() - 1);
                String[] pairs = body.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (String pair : pairs) {
                    String[] kv = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
                    if (kv.length == 2) {
                        String key = kv[0].trim().replaceAll("^\"|\"$", "");
                        String val = kv[1].trim().replaceAll("^\"|\"$", "");
                        map.put(key, val);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
