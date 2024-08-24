package com.wefood.front.global.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.wefood.front.global.service.GptService;
import com.wefood.front.global.dto.GptModelsResponseDto;
import com.wefood.front.global.dto.GptResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * class: ChatGptController.
 *
 * @author JBumLee
 * @version 2024/08/21
 */
@Controller
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    @Value("${openai.endpoint.gpt-free}")
    private String endpointFree;

    @Value("${openai.endpoint.gpt-charged}")
    private String endpointCharged;

    private final GptService chatGPTService;

    @GetMapping("/models")
    public ResponseEntity<GptModelsResponseDto> selectModelList() {
        return ResponseEntity.ok().body(chatGPTService.models());
    }

    @PostMapping("/chat")
    @ResponseBody
    public JsonNode chat(
            @NotNull @RequestParam("model") String model,
            @NotNull @RequestParam("isProduct") boolean isProduct,
            @NotNull @RequestParam("input") String... input) {

        return chatGPTService.chat(model, endpointCharged, isProduct,input);  // 추가 데이터 예시
    }

    @PostMapping("/farm")
    public ResponseEntity<Map<String, String>> farmChat(
            @NotNull @RequestParam("model") String model,
            @NotNull @RequestParam("isProduct") boolean isProduct,
            @NotNull @RequestParam("input") String input) {


        String response = chatGPTService.farmChat(model, endpointCharged, isProduct, input);
        System.out.println("결과 값: " + response);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);

        return ResponseEntity.ok(result);
    }

}