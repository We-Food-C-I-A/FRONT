package com.wefood.front.chat_bot.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.product.dto.UploadThumbnailRequestDto;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * class: ChatBotAdaptor.
 *
 * @author JBumLee
 * @version 2024/08/21
 */

@Component
public class ChatBotAdaptor {
    private final RestTemplate restTemplate;
    private final BackAdaptorProperties backAdaptorProperties;
    private static final String PRODUCT_URL = "/api/product";
    private static final String FARM_URL = "/api/farm";

    private static final String THUMBNAIL = "/thumbnail";

    public ChatBotAdaptor(@Qualifier("restTemplate") RestTemplate restTemplate, BackAdaptorProperties backAdaptorProperties) {
        this.restTemplate = restTemplate;
        this.backAdaptorProperties = backAdaptorProperties;
    }

    public void sendThumbnailRequest(Long id, MultipartFile multipartFile, boolean isProduct)
        throws IOException {
        String url = isProduct?PRODUCT_URL:FARM_URL;

        URI uri = UriComponentsBuilder
            .fromUriString(backAdaptorProperties.getAddress())
            .path(url+THUMBNAIL)
            .encode()
            .build()
            .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

        fileToByteArrayResource(multipartFile, multiValueMap, httpHeaders);

        restTemplate.exchange(
            uri,
            HttpMethod.POST,
            new HttpEntity<>(multiValueMap, httpHeaders),
            new ParameterizedTypeReference<>() {
            }
        );

    }

    public void sendImageRequest(UploadImageRequestDto requestDto, boolean isProduct) throws IOException {
        // URL 선택: 제품인지 농장인지에 따라 다름
        String url = isProduct ? PRODUCT_URL : FARM_URL;

        // HTTP 헤더 설정: 멀티파트 폼 데이터로 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        // URI 빌드
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress())
            .path(url)
            .build()
            .toUri();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("id", requestDto.getId());

        List<MultipartFile> files = requestDto.getFiles();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename(); // 원래 파일 이름을 반환하도록 설정
                    }
                };
                body.add("files", resource); // 각 파일을 하나씩 추가
            }
        }

// HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

// HttpEntity 생성
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

// 요청 보내기
        ResponseEntity<Map<String, Object>> result = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<Map<String, Object>>() {
            }
        );
    }
    private void fileToByteArrayResource(MultipartFile image,
        MultiValueMap<String, Object> multiValueMap, HttpHeaders multipartHeader)
        throws IOException {
        if (image != null) {
            ByteArrayResource thumbnailAsResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            multiValueMap.add("files", new HttpEntity<>(thumbnailAsResource, multipartHeader));
        }
    }
}
