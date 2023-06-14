package com.example.demo.service;

import com.example.demo.dto.textDTO;

import java.util.List;

public interface azureLanguageService {
    public String summarize(String text);
    public String keyPhraseExtract(List<textDTO> text);
    public List<textDTO> languageDetect(String[] texts);
}
