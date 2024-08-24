package com.wefood.front.chat_bot.adaptor;

import com.wefood.front.config.BackAdaptorProperties;
import com.wefood.front.product.dto.UploadImageRequestDto;
import com.wefood.front.product.dto.UploadThumbnailRequestDto;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public void sendThumbnailRequest(UploadThumbnailRequestDto requestDto, boolean isProduct)
        throws IOException {
        String url = isProduct?PRODUCT_URL:FARM_URL;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress())
            .path(url+THUMBNAIL)
            .build()
            .toUri();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", new ByteArrayResource(requestDto.getFiles().getBytes()) {
            @Override
            public String getFilename() {
                return requestDto.getFiles().getOriginalFilename();
            }
        });
        body.add("id",requestDto.getId());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                Void.class
            );

            if (response.getStatusCode() != HttpStatus.CREATED) {
                throw new IOException("Failed to upload thumbnail. Status code: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new IOException("Error writing request body to server", e);
        }
    }

    public void sendImageRequest(UploadImageRequestDto requestDto, boolean isProduct) throws IOException {
        // URL 선택: 제품인지 농장인지에 따라 다름
        String url = isProduct ? PRODUCT_URL : FARM_URL;

        // HTTP 헤더 설정: 멀티파트 폼 데이터로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // URI 빌드
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress())
            .path(url)
            .build()
            .toUri();

        // 요청 본문 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("id", requestDto.getId());

        // 여러 파일 추가
        List<MultipartFile> files = requestDto.getFiles();
        for (MultipartFile file : files) {
            body.add("files", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        }

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // 서버로 요청 보내기
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                Void.class
            );

            // 응답 상태 확인
            if (response.getStatusCode() != HttpStatus.CREATED) {
                throw new IOException("Failed to upload thumbnail. Status code: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // 예외 처리
            throw new IOException("Error writing request body to server", e);
        }
    }
}
