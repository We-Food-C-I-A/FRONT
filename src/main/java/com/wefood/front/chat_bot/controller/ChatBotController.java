package com.wefood.front.chat_bot.controller;

import com.wefood.front.chat_bot.dto.CreateProductRequestDto;
import com.wefood.front.chat_bot.service.ChatBotService;
import com.wefood.front.product.service.ProductService;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.response.FarmResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * class: ChatBotController.
 *
 * @author JBumLee
 * @version 2024/08/21
 */

@Controller
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;
    private final UserAdaptor userAdaptor;
    private final ProductService productService;
    private final RestTemplate restTemplate;
    private final static boolean PRODUCT = true;

    @PostMapping("/productImage")
    public ResponseEntity<String> productImageUpload(
        @RequestParam("id") Long id,
        @RequestParam("files") List<MultipartFile> files) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("id", id);

        // 파일 리스트를 순회하면서 각 파일을 body에 추가합니다.
        for (MultipartFile file : files) {
            // MultipartFile의 getResource() 메서드를 사용하여 Resource로 변환하여 추가합니다.
            body.add("files", file.getResource());
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://api.we-food.store/api/product", entity, String.class);

        return response;
    }

    @PostMapping("/farmImage")
    public ResponseEntity<String> farmImageUpload(
        @RequestParam("id") Long id,
        @RequestParam("files") List<MultipartFile> files) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("id", id);

        // 파일 리스트를 순회하면서 각 파일을 body에 추가합니다.
        for (MultipartFile file : files) {
            body.add("files", file.getResource());
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://api.we-food.store/api/farm", entity, String.class);

        return response;
    }


    @PostMapping("/product/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveFile(@RequestParam("id") Long id,
        @RequestParam MultipartFile files) {
//        chatBotService.setThumbnail(id, image, true);
//        return new ResponseEntity(HttpStatus.CREATED);
            //RestTemplate을 이용한 단일 파일 업로드
            HttpHeaders headers = new HttpHeaders(); //헤더와 본문이 있는 HttpEntity를 만든다.
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); //헤더값을 설정해주면 RestTemplate은 일부 메타 데이터와 함께 파일 데이터를 자동으로 마샬링 한다.

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(); //LinkedMultiValueMap은 LinkedList의 각 키에 대해 여러 값을 저장하는 LinkedHashMap을 래핑
            body.add("id", id);
            body.add("files", files.getResource()); //리소스 보내기

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(body, headers); //헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.we-food.store/api/product/thumbnail", entity, String.class); //restTemplate.postForEntity()
            return response;
    }

    @PostMapping("/farm/thumbnail")
    public ResponseEntity<String> farmThumbnailUpload(@RequestParam("id") Long id,
        @RequestParam("files") MultipartFile files) {
        HttpHeaders headers = new HttpHeaders(); //헤더와 본문이 있는 HttpEntity를 만든다.
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); //헤더값을 설정해주면 RestTemplate은 일부 메타 데이터와 함께 파일 데이터를 자동으로 마샬링 한다.

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(); //LinkedMultiValueMap은 LinkedList의 각 키에 대해 여러 값을 저장하는 LinkedHashMap을 래핑
        body.add("id", id);
        body.add("files", files.getResource()); //리소스 보내기

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(body, headers); //헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.we-food.store/api/farm/thumbnail", entity, String.class); //restTemplate.postForEntity()
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "/test";
    }

    @GetMapping("/modalContent1")
    @ResponseBody  // JSON 응답을 반환하기 위해 추가
    public Map<String, Object> getModalContent1(@CookieValue(name = "id") String id) {
        FarmResponse farmResponse = userAdaptor.getFarm(Long.parseLong(id));
        List<String> quest = List.of(
            "상품의 품종은 무엇인가요?",
            "상품만의 특색 혹은 강조하고 싶은 점이 있다면 입력해 주세요. <br> (우리 포도는 깨끗한 환경에서 친환경적으로 재배했어요. 그래서 아주 달콤합니다.)"
        );
        Map<Integer, String> categories = Map.of(1, "못난이 농산물", 2, "신품종 상품", 3, "농작물", 4, "축산물", 5,
            "가공식품");
        // 데이터를 Map으로 구성하여 JSON으로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("farm", farmResponse);
        response.put("quest", quest);
        response.put("categories", categories);
        return response;
    }

    @PostMapping("/create-product")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, Object> createProduct(
        @RequestBody CreateProductRequestDto createProductRequestDto) {
        Map<String, Object> response = new HashMap<>();
        response.put("productId", productService.create(createProductRequestDto));
        return response;
    }
}