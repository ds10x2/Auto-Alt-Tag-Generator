package com.example.demo.mapper;

import com.example.demo.dto.imageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface imageMapper {
    void createImage(imageDTO image) throws Exception;
    imageDTO readImageByContent(String content) throws Exception;
}
