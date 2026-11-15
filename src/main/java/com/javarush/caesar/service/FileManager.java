package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public String readFile(String filePath) throws CipherException {
        try {
            Path path = Path.of(filePath).toAbsolutePath();
            return Files.readString(path);
        } catch (IOException e) {
            throw new CipherException("Не удалось прочитать файл: " + filePath + ". Проверьте корректность кодировки: UTF-8.");
        }
    }

    public void writeFile(String content, String filePath) throws CipherException {
        Path path = Path.of(filePath).toAbsolutePath();
        if (Files.exists(path)) {
            throw new CipherException("Результат нельзя записать в исходный файл: " + path);
        }
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CipherException("Не удалось записать данные в файл: " + path, e);
        }
    }

    }

