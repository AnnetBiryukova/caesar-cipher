package com.javarush.caesar.service;
import com.javarush.caesar.exception.CipherException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Validator {

    public boolean isValidKey(int key, List<Character> alphabet) {
        if (alphabet == null || alphabet.isEmpty()) {
            return false;
        }
        return key >= 0 && key < alphabet.size();
    }

    public boolean isFileExists(String filePath) throws CipherException {
        if (filePath == null) {
            return false;
        }
        try {
            Path path = Path.of(filePath);
            return Files.exists(path);
        } catch (Exception e) {
            throw new CipherException("Файла по указанному пути не существует: " + filePath);
        }
    }

    public boolean isValidOutputPath(String filePath) throws CipherException {
        if (filePath == null) {
            return false;
        }
        try {
            Path path = Path.of(filePath).toAbsolutePath().normalize();
            if (Files.isDirectory(path)) {
                return false;
            }
            Path parent = path.getParent();
            return parent != null && Files.exists(parent);
        } catch (Exception e) {
            throw new CipherException("Путь некорректный или файл по указанному пути уже существует.");
        }
    }
}
