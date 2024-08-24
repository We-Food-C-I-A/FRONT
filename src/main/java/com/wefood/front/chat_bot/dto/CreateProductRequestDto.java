package com.wefood.front.chat_bot.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * class: CreateProductRequestDto.
 *
 * @author JBum
 * @version 2024/08/23
 */
@Getter
@ToString
@Setter
public class CreateProductRequestDto {
    private Long categoryId;
    private List<String> tagName;
    private String title;
    private String detail;
    private Long farmId;
    private int price;
}
