package com.aplikasi.challenge.utils;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Response {
    public Map<Object, Object> success(Object data) {
        Map<Object, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    public Map<Object, Object> error(String message) {
        Map<Object, Object> map = new HashMap<>();
        map.put("error", message);
        map.put("code", 404);
        return map;
    }
}
