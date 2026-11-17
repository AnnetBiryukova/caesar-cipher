package com.javarush.caesar.service;

import com.javarush.caesar.core.Cipher;
import com.javarush.caesar.exception.CipherException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    private static final int BLOCK_SIZE = 8192;

    public String readFile(String filePath) throws CipherException {
        try {
            Path path = Path.of(filePath).toAbsolutePath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CipherException(
                    "Не удалось прочитать файл: " + filePath + ". Проверьте кодировку UTF-8.", e);
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

    public void encryptFile(String sourcePath, String resultPath, Cipher cipher, int key)
            throws CipherException {
        processFile(sourcePath, resultPath, cipher, key, true);
    }

    public void decryptFile(String sourcePath, String resultPath, Cipher cipher, int key)
            throws CipherException {
        processFile(sourcePath, resultPath, cipher, key, false);
    }

    private void processFile(String sourcePath, String resultPath, Cipher cipher,
                             int key, boolean decrypt) throws CipherException {

        Path source = Path.of(sourcePath).toAbsolutePath();
        Path result = Path.of(resultPath).toAbsolutePath();

        if (Files.exists(result)) {
            throw new CipherException("Результат нельзя записать в исходный файл: " + result);
        }

        try {
            Path parent = result.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            try (BufferedReader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
                 BufferedWriter writer = Files.newBufferedWriter(result, StandardCharsets.UTF_8)) {

                char[] buffer = new char[BLOCK_SIZE];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    String block = new String(buffer, 0, read);
                    String processed = decrypt
                            ? cipher.decrypt(block, key)
                            : cipher.encrypt(block, key);
                    writer.write(processed);
                }
            }
        } catch (IOException e) {
            throw new CipherException("Не удалось обработать файл: " + source, e);
        }
    }

}

