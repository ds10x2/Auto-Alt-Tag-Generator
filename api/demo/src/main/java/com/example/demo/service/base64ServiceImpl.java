package com.example.demo.service;

import com.example.demo.dto.base64ImageDTO;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class base64ServiceImpl implements base64Service {
    @Override
    public int store(base64ImageDTO base64Image) {
        byte[] data = Base64.decodeBase64(base64Image.getBase64String().getBytes());
        try(OutputStream stream = new FileOutputStream("temp.bmp")) {
            stream.write(data);
            return 0;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}
