package com.example.demo.controller;

import com.example.demo.dto.base64ImageDTO;
import com.example.demo.dto.imageDTO;
import com.example.demo.dto.urlImageDTO;
import com.example.demo.service.imageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/image")
public class imageController {
    @Autowired
    private imageService imageService;
/*
    @PostMapping("/urlImage")
    public ResponseEntity<imageDTO> createImage(@RequestHeader HttpHeaders requestHeader, @RequestBody urlImageDTO urlImage) throws Exception {
        imageService.createImage(urlImage);
        imageDTO responseBody = imageService.readImageByContent(urlImage.getUrl());
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.add("Access-Control-Allow-Origin", requestHeader.getOrigin());
        responseHeader.add("Access-Control-Allow-Methods", "POST");
        responseHeader.add("Access-Control-Allow-Headers", "Content-Type");
        responseHeader.add("Access-Control-Max-Age", "600");
        return new ResponseEntity<imageDTO>(responseBody, responseHeader, HttpStatus.OK);
    }
    @PostMapping("/base64Image")
    public ResponseEntity<imageDTO> createImage(@RequestHeader HttpHeaders requestHeader, @RequestBody base64ImageDTO base64Image) throws Exception {
        imageService.createImage(base64Image);
        imageDTO responseBody = imageService.readImageByContent(base64Image.getBase64String());
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.add("Access-Control-Allow-Origin", requestHeader.getOrigin());
        responseHeader.add("Access-Control-Allow-Methods", "POST");
        responseHeader.add("Access-Control-Allow-Headers", "Content-Type");
        responseHeader.add("Access-Control-Max-Age", "600");
        return new ResponseEntity<imageDTO>(responseBody, responseHeader, HttpStatus.OK);
    }
 */
    @PostMapping("/urlImage")
    public imageDTO createImage(@RequestBody urlImageDTO urlImage) throws Exception {
        imageService.createImage(urlImage);
        return imageService.readImageByContent(urlImage.getUrl());
    }
    @PostMapping("/base64Image")
    public imageDTO createImage(@RequestBody base64ImageDTO urlImage) throws Exception {
        imageService.createImage(urlImage);
        return imageService.readImageByContent(urlImage.getBase64String());
    }
}
