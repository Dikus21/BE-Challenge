package com.aplikasi.challenge.service.oauth;

import com.aplikasi.challenge.config.Config;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateCRUD {
    Config config = new Config();
    public Map templateSukses(Object obj)  {
        Map map = new HashMap();
        try {
            map.put("data",obj );
            map.put(config.getCode(), config.code_sukses);
            map.put(config.getMessage(), config.message_sukses);
            return map;

        } catch (Exception e) {
            System.out.println("eror template1 ="+e);
            map.put(config.getCode(), config.code_server);
            map.put(config.getMessage(), e.getMessage());
            return map;
        }

    }

    public Map templateEror(String obj)  {
        Map map = new HashMap();
        map.put(config.getCode(), config.code_server);
        map.put(config.getMessage(), obj);
        return map;
    }

    public Map notFound(String obj)  {
        Map map = new HashMap();
        map.put(config.getCode(), config.code_notFound);
        map.put(config.getMessage(), obj);
        return map;
    }

    public Map isRequired(String obj)  {
        Map map = new HashMap();
        map.put(config.getCode(), config.code_notFound);
        map.put(config.getMessage(), obj);
        return map;
    }


}

