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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        System.out.println("id: " + id);
        files.stream().forEach((s) -> System.out.println(s.getOriginalFilename()));
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
    public ResponseEntity<HttpStatus> saveFile(@RequestParam("id") Long id,
        @RequestParam MultipartFile image) {
        chatBotService.setThumbnail(id, image, true);
        return new ResponseEntity(HttpStatus.CREATED);

    }
}