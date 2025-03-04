//package com.aplikasi.challenge.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.print.attribute.standard.Media;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class ProductImplTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    private String token;
//    @BeforeEach
//    void setUp() throws Exception {
//        // Need to set the non expire token here to be able to run the test
//        token = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXJAbWFpbC5jb20iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNzQxMDM0MDAzLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiS0xKUmtUTEZDamdiMUxFeUZCNS1Wb1hYRmFrIiwiY2xpZW50X2lkIjoibXktY2xpZW50LXdlYiJ9.DHuWCztSOU9XuhrtHj-Mwyhv6tQ8dYU-Riq-faQBWtQ ";
//    }
//
//    @Test
//    void saveTest() throws Exception {
//        Map sample = new HashMap<>();
//        Map sampleMerchant = new HashMap<>();
//        sampleMerchant.put("id", "1");
//        sample.put("name", "test product");
//        sample.put("price", "5000000");
//        sample.put("merchant", sampleMerchant);
//
//        System.out.println("Positive Case : ");
//        mockMvc.perform(
//                post("/v1/product/save")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Save Result :" + result.getResponse().getContentAsString());
//        });
////        System.out.println("Negative Case : ");
//        sampleMerchant.put("id", "1");
//        sample.put("name", null);
//        mockMvc.perform(
//                post("/v1/product/save")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Save Result :" + result.getResponse().getContentAsString());
//        });
//        sample.put("name", "test product");
//        sample.put("price", null);
//        sampleMerchant.put("id", "1");
//        sample.put("name", null);
//        mockMvc.perform(
//                post("/v1/product/save")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Save Result :" + result.getResponse().getContentAsString());
//        });
//    }
//
//    @Test
//    void updateTest() throws Exception {
//        Map sample = new HashMap<>();
//        sample.put("id", "1");
//        sample.put("name", "test product update");
//        sample.put("price", "6000000");
//        System.out.println("Positive Case : ");
//        mockMvc.perform(
//                put("/v1/product/update")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Update Result :" + result.getResponse().getContentAsString());
//        });
//        System.out.println("Negative Case : ");
//        sample.put("id", "1");
//        mockMvc.perform(
//                put("/v1/product/update")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Update Result :" + result.getResponse().getContentAsString());
//        });
//        sample.put("id", "1");
//        sample.put("name", null);
//        mockMvc.perform(
//                put("/v1/product/update")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Update Result :" + result.getResponse().getContentAsString());
//        });
//        sample.put("name", "test product update");
//        sample.put("price", null);
//        mockMvc.perform(
//                put("/v1/product/update")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Update Result :" + result.getResponse().getContentAsString());
//        });
//    }
//
//    @Test
//    void deleteTest() throws Exception {
//        Map sample = new HashMap<>();
//        sample.put("id", "1");
//        System.out.println("Positive Case : ");
//        mockMvc.perform(
//                delete("/v1/product/delete")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Delete Result :" + result.getResponse().getContentAsString());
//        });
//        System.out.println("Negative Case : ");
//        sample.put("id", "1");
//        mockMvc.perform(
//                delete("/v1/product/delete")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sample))
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Delete Result :" + result.getResponse().getContentAsString());
//        });
//    }
//
//    @Test
//    void getByIdTest() throws Exception {
//        String idSample =  "1";
//        System.out.println("Positive Case : ");
//        mockMvc.perform(
//                get("/v1/product/" + idSample)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Get Id Result :" + result.getResponse().getContentAsString());
//        });
//        System.out.println("Negative Case : ");
//        idSample = "1";
//        mockMvc.perform(
//                get("/v1/product/" + idSample)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            System.out.println("Get Id Result :" + result.getResponse().getContentAsString());
//        });
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//}