package com.mysite.crud.services;

import com.mysite.crud.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    //JPA 하이버네이트가 CRUD 자동으로 생성
}
