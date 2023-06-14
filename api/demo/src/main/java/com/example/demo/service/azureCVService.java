package com.example.demo.service;

import com.example.demo.dto.base64ImageDTO;
import com.example.demo.dto.urlImageDTO;

public interface azureCVService {
    public String captionImageV4(urlImageDTO urlImage);
    public String ocrImageV4(urlImageDTO urlImage);
    public String captionImageV4();
    public String ocrImageV4();
}
