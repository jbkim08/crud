package com.mysite.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping({"","/"})
    public String showProductList(Model model) {
        System.out.println("리스트 페이지.");
        return "products/index";
    }

}
