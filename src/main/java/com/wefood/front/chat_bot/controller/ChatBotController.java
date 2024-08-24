package com.wefood.front.chat_bot.controller;

import com.wefood.front.chat_bot.dto.CreateProductRequestDto;
import com.wefood.front.chat_bot.service.ChatBotService;
import com.wefood.front.product.service.ProductService;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.response.FarmResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private final static boolean PRODUCT = true;

    @PostMapping("/productImage")
    public String productImageUpload(@RequestParam("id") Long id,
        @RequestParam("files") List<MultipartFile> files) {
        System.out.println("id: "+id);
        files.stream().forEach((s)-> System.out.println(s.getName()));
        chatBotService.setImages(id, files, PRODUCT);
        return "/index";
    }

    @PostMapping("/farmImage")
    public String farmImageUpload(@RequestParam("id") Long id,
        @RequestParam("files") List<MultipartFile> files) {
        chatBotService.setImages(id, files, !PRODUCT);
        return "/index";
    }

    @PostMapping("/product/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public void productThumbnailUpload(@RequestParam("id") Long id,
        @RequestParam("files") MultipartFile files) {
        chatBotService.setThumbnail(id, files, PRODUCT);
    }

    @PostMapping("/farm/thumbnail")
    public String farmThumbnailUpload(@RequestParam("id") Long id,
        @RequestParam("files") MultipartFile files) {
        chatBotService.setThumbnail(id, files, !PRODUCT);
        return "/index";
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
        Map<Integer,String> categories = Map.of(1,"못난이 농산물", 2,"신품종 상품",3,"농작물",4,"축산물",5,"가공식품");
        // 데이터를 Map으로 구성하여 JSON으로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("farm", farmResponse);
        response.put("quest", quest);
        response.put("categories",categories);
        return response;
    }

    @PostMapping("/create-product")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, Object> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto){
        Map<String, Object> response = new HashMap<>();
        response.put("productId",productService.create(createProductRequestDto));
        return response;
    }
}