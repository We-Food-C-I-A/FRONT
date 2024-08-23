package com.wefood.front.chat_bot.service;

import com.wefood.front.chat_bot.adaptor.ChatBotAdaptor;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.product.dto.UploadThumbnailRequestDto;
import java.io.IOException;
import java.util.List;
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
        try {
            chatBotAdaptor.sendImageRequest(dto,isProduct);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThumbnail(Long id, MultipartFile multipartFile,boolean isProduct) {
        UploadThumbnailRequestDto dto = new UploadThumbnailRequestDto();
        dto.setFiles(multipartFile);
        dto.setId(id);
        try {
            chatBotAdaptor.sendThumbnailRequest(dto,isProduct);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
