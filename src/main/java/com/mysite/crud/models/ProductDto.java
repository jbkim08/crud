package com.mysite.crud.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

//Dto 는 유저의 product 입력을 받는 객체를 만들기 위함
@Getter
@Setter
public class ProductDto {

    @NotEmpty(message = "이름을 입력하세요")
    private String name;        //제품명
    @NotEmpty(message = "브랜드를 입력하세요")
    private String brand;       //브랜드
    @NotEmpty(message = "카테고리를 입력하세요")
    private String category;    //카테고리
    @Min(0)
    private int price;          //가격

    @Size(min = 10, message = "제품설명은 10자 이상")
    @Size(max = 100, message = "제품설명은 100자 이하")
    private String description; //제품설명
    //이미지 파일 (실제 업로드 파일)
    private MultipartFile imageFile; //이미지파일
}
