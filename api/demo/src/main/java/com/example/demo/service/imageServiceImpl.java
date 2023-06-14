package com.example.demo.service;

import com.example.demo.dto.base64ImageDTO;
import com.example.demo.dto.imageDTO;
import com.example.demo.dto.textDTO;
import com.example.demo.dto.urlImageDTO;
import com.example.demo.mapper.imageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.Math.min;

@Service
public class imageServiceImpl implements imageService {
    @Autowired
    private imageMapper imageMapper;
    @Autowired
    azureCVService azureCVService;
    @Autowired
    azureLanguageService azureLanguageService;
    @Autowired
    papagoService papagoService;
    @Autowired
    base64Service base64Service;

    @Override
    @Transactional
    public void createImage(urlImageDTO urlImage) throws Exception {
        String url = urlImage.getUrl();
        String content = url.substring(0, min(200, url.length()));

        imageDTO image = readImageByContent(content);
        String text = azureCVService.captionImageV4(urlImage);
        if(image != null) return;

        image = new imageDTO();
        if(text == "text") {
            text = azureCVService.ocrImageV4(urlImage);
            String[] texts = text.split("\n");
            List<textDTO> textList = azureLanguageService.languageDetect(texts);
            int koCnt = 0;
            int enCnt = 0;
            for(int i = 0; i < textList.size(); i++) {
                String language = textList.get(i).getLanguage();
                if(language == "ko") koCnt++;
                else if(language == "en") enCnt++;
                else textList.remove(i--);
            }
            text = "";
            if (koCnt > 0) {
                Predicate<textDTO> textPredicate = textDto -> !textDto.getLanguage().equals("ko");
                textList.removeIf(textPredicate);
            }
            else {
                Predicate<textDTO> textPredicate = textDto -> !textDto.getLanguage().equals("en");
                textList.removeIf(textPredicate);
            }
            for(int i = 0; i < textList.size(); i++)
                text = text + textList.get(i).getText() + "\n";
            image.setType("text");
            System.out.println(text);
        }
        else {
            text = papagoService.translate(text);
            image.setType("image");
        }
        image.setContent(content);
        image.setAltText(text);
        if(text != "") imageMapper.createImage(image);
    }
    @Override
    @Transactional
    public void createImage(base64ImageDTO base64Image) throws Exception {
        String base64String = base64Image.getBase64String();
        String content = base64String.substring(0, min(200, base64String.length()));

        imageDTO image = readImageByContent(content);
        if(image != null) return;

        image = new imageDTO();
        base64Service.store(base64Image);
        String text = azureCVService.captionImageV4();
        if(text == "text") {
            text = azureCVService.ocrImageV4();
            image.setType("text");
        }
        else {
            text = papagoService.translate(text);
            image.setType("image");
        }
        image.setContent(content);
        image.setAltText(text);
        imageMapper.createImage(image);
    }
    @Override
    public imageDTO readImageByContent(String content) throws Exception {
        return imageMapper.readImageByContent(content.substring(0, min(200, content.length())));
    }
}
