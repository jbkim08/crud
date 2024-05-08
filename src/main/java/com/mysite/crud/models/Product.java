package com.mysite.crud.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;       //제품번호

    private String name;  //제품명
    private String brand; //브랜드
    private String category; //카테고리
    private int price;      //가격

    @Column(columnDefinition = "TEXT")
    private String description; //제품설명

    @CreationTimestamp
    private Date createAt;      //등록일자

    private String imageFileName; //이미지파일 이름
}
