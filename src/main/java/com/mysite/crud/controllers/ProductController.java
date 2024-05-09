package com.mysite.crud.controllers;

import com.mysite.crud.models.Product;
import com.mysite.crud.models.ProductDto;
import com.mysite.crud.services.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @GetMapping({"","/"})
    public String showProductList(Model model) {
        List<Product> productList = repo.findAll();
        model.addAttribute("products", productList);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        ProductDto dto = new ProductDto(); //빈 제품 객체를 생성
        model.addAttribute("productDto", dto); //form 에 전달
        return "products/createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid ProductDto productDto,
                                BindingResult bindingResult) {
        //이미지 파일이 없으면 바인딩리절트에 에러를 추가한다.
        if(productDto.getImageFile().isEmpty()){
            bindingResult.addError(new FieldError("productDto", "imageFile","이미지를 올려주세요"));
        }

        if (bindingResult.hasErrors()) {
            //에러가 나면 되돌아감.
            return "products/createProduct";
        }
        //에러가 없으면 제품을 저장
        MultipartFile image = productDto.getImageFile(); //이미지파일
        Date createDate = new Date();
        //파일 이름이 중복되지 않도록 현재시간을 ms로 바꿔서 파일명 앞에 시간_파일.png
        String storeFileName = createDate.getTime() + "_" + image.getOriginalFilename();
        //이미지파일을 public/images 폴더에 저장함
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storeFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageFileName(storeFileName); //파일의 이름만 저장한다.

        repo.save(product); //DB에 저장됨

        //리스트 페이지로
        return "redirect:/products";
    }

    //수정 페이지 보이기
    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam int id) {

        try {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        return "products/editProduct";
    }

    //수정하기
    @PostMapping("/edit")
    public String editProduct(@Valid ProductDto productDto, BindingResult bindingResult,
                              @RequestParam int id, Model model) {

        Product product = repo.findById(id).get();
        model.addAttribute("product", product);

        if (bindingResult.hasErrors()) {
            return "products/editProduct";
        }
        //수정할 이미지 있으면 기존이미지 삭제하고 수정 이미지를 업로드함
        if(!productDto.getImageFile().isEmpty()){
            String uploadDir = "public/images/";
            Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

            try {
                Files.delete(oldImagePath);
            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            //새 이미지 업로드
            MultipartFile image = productDto.getImageFile();
            Date createDate = new Date();
            String storeFileName = createDate.getTime() + "_" + image.getOriginalFilename();

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir+storeFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            product.setImageFileName(storeFileName); //이미지 파일 이름을 업데이트
        }
        //이미지 제외한 수정 내용을 업데이트
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());

        repo.save(product); //수정이 완료된 제품객체로 DB 업데이트함

        return "redirect:/products/";
    }

    //삭제하기
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {

        try {
            Product product = repo.findById(id).get();
            //이미지 파일 삭제
            String uploadDir = "public/images/";
            Path imagePath = Paths.get(uploadDir + product.getImageFileName());
            Files.delete(imagePath);
            //DB 에서 제품 삭제
            repo.delete(product);

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        return "redirect:/products";
    }

}







