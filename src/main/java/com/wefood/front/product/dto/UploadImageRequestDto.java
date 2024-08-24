package com.wefood.front.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * class: UploadImageDto.
 *
 * @author JBumLee
 * @version 2024/08/11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadImageRequestDto {
    private List<MultipartFile> files;

    private Long id;
}