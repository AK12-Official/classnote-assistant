package com.classnote.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.classnote.backend.dto.DeepSeekResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeepSeekService {
    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Value("${deepseek.prompt.template}")
    private String promptTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    public String processText(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String prompt = String.format(promptTemplate, text);
        // 避免JSON格式化问题，使用转义
        prompt = prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");

        // 使用Jackson或其他库生成JSON可以避免格式问题
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "deepseek-chat");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);

        requestMap.put("messages", messages);
        requestMap.put("temperature", 0.7);

        // 使用 ObjectMapper 转换为 JSON
        ObjectMapper mapper = new ObjectMapper();
        String requestBody;

        try {
            requestBody = mapper.writeValueAsString(requestMap);
        } catch (JsonProcessingException e) {
            return "生成请求失败：" + e.getMessage();
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            // 添加日志输出以便调试
            System.out.println("发送请求到: " + apiUrl);
            System.out.println("请求体: " + requestBody);

            DeepSeekResponse response = restTemplate.postForObject(apiUrl, request, DeepSeekResponse.class);
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
            return "无法获取有效响应";
        } catch (Exception e) {
            return "处理失败：" + e.getMessage();
        }
    }
}