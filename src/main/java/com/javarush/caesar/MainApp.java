package com.javarush.caesar;

import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.Cipher;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.util.*;

public class MainApp {

    private static final List ALPHABET = Arrays.asList(
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' ', '\n', '\r', '\t');


    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final Validator validator = new Validator();
    private final Cipher cipher = new Cipher(ALPHABET);
    private final BruteForce bruteForce = new BruteForce();


    public static void main(String[] args) {
        new MainApp().run();
    }

    private void run() {
        System.out.println("Шифр Цезаря: шифрование и расшифровка файлов");
        while (true) {
            printMenu();
            String choice = ask("Выберите действие: ");
            try {
                switch (choice) {
                    case "1":
                        encryptFile();
                        break;
                    case "2":
                        decryptFile();
                        break;
                    case "3":
                        bruteForceFile();
                        break;
                    case "0":
                    case "exit":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Нет такого пункта: " + choice);
                }
            } catch (CipherException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Зашифровать файл");
        System.out.println("2. Расшифровать файл с ключом");
        System.out.println("3. Взломать перебором ключей");
        System.out.println("0. Выход (или exit)");
    }

    private String askForSourceFile() throws CipherException {
        String path = ask("Введите путь к исходному файлу");
        if (!validator.isFileExists(path)) {
            throw new CipherException("Указан некорректный путь к файлу: " + path);
        }
        return path;
    }

    private String askForResultFile() throws CipherException {
        String path = ask("Введите путь к результирующему файлу");
        if (!validator.isValidOutputPath(path)) {
            throw new CipherException("Указан некорректный путь к файлу: " + path);
        }
        return path;
    }

    private int askForKey() throws CipherException {
        String keyString = ask("Введите ключ для шифровки. Ключ должен быть целым числом");
        int key;
        try {
            key = Integer.parseInt(keyString);
        } catch (NumberFormatException e) {
            throw new CipherException("Ключ должен быть числовым значением");
        }
        if (!validator.isValidKey(key, ALPHABET)) {
            throw new CipherException("Ключ должен быть в диапазоне 0.." + (ALPHABET.size() - 1));
        }
        return key;
    }

    private void encryptFile() throws CipherException {
        String sourcePath = askForSourceFile();
        int key = askForKey();
        String resultFile = askForResultFile();
        fileManager.encryptFile(sourcePath, resultFile, cipher, key);
        System.out.println("Файл зашифрован и сохранён: " + resultFile);
    }

    private void decryptFile() throws CipherException {
        String sourcePath = askForSourceFile();
        int key = askForKey();
        String resultFile = askForResultFile();
        fileManager.decryptFile(sourcePath, resultFile, cipher, key);
        System.out.println("Файл расшифрован и сохранён: " + resultFile);
    }

    private void bruteForceFile() throws CipherException {
        String encryptedText = fileManager.readFile(askForSourceFile());
        String resultFile = askForResultFile();
        fileManager.writeFile(bruteForce.decryptByBruteForce(encryptedText, ALPHABET), resultFile);
        System.out.println("Файл расшифрован перебором и сохранён: " + resultFile);
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
