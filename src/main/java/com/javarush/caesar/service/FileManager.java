package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public String readFile(String filePath) throws CipherException {
        try {
            Path path = Path.of(filePath);
            return Files.readString(path);
        } catch (IOException e) {
            throw new CipherException("Не удалось прочитать файл: " + filePath + ". Проверьте корректность кодировки: UTF-8.");
        }
    }

    public void writeFile(String content, String filePath) throws CipherException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.writeString(path, content);
            } catch (IOException e) {
                throw new CipherException("Не удалось записать данные в файл: " + filePath, e);
            }
        } else {
            throw new CipherException("Результат нельзя записать в исходный файл.");
        }
    }
}
