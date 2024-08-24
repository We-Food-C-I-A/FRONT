package com.wefood.front.product.adaptor;

import com.wefood.front.chat_bot.dto.CreateProductRequestDto;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * class: CreateProductRequest.
 *
 * @author JBum
 * @version 2024/08/24
 */

@Getter
@ToString
public class CreateProductRequest {
    private final String name;
    private final List<String> tags;
    private final Long categoryId;
    private final String detail;
    private final int price;

    public CreateProductRequest(CreateProductRequestDto createProductRequestDto) {
        this.name = createProductRequestDto.getTitle();
        this.tags = createProductRequestDto.getTagName();
        this.categoryId = createProductRequestDto.getCategoryId();
        this.detail = createProductRequestDto.getDetail();
        this.price=createProductRequestDto.getPrice();
    }
}
