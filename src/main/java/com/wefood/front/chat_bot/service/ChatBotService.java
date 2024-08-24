package com.wefood.front.chat_bot.service;

import com.wefood.front.chat_bot.adaptor.ChatBotAdaptor;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.product.dto.UploadThumbnailRequestDto;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * class: ChatBotService.
 *
 * @author JBumLee
 * @version 2024/08/21
 */

@Service
public class ChatBotService {
    private final ChatBotAdaptor chatBotAdaptor;

    public ChatBotService(ChatBotAdaptor chatBotAdaptor) {
        this.chatBotAdaptor = chatBotAdaptor;
    }

    public void setImages(Long id, List<MultipartFile> multipartFile,boolean isProduct) {
        UploadImageRequestDto dto = new UploadImageRequestDto(multipartFile,id);
        System.out.println(dto.toString());
        try {
            chatBotAdaptor.sendImageRequest(dto,isProduct);
            System.out.println("어댑터끝");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThumbnail(Long id, MultipartFile multipartFile,boolean isProduct) {
        try {
            chatBotAdaptor.sendThumbnailRequest(id,multipartFile,isProduct);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
