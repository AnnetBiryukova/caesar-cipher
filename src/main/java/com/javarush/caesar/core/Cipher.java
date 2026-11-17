package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cipher {

    private final List<Character> characterList;
    private final Map<Character, Integer> indexByChar;

    public Cipher(List<Character> characterList) {
        this.characterList = characterList;
        this.indexByChar = new HashMap<>();
        for (int i = 0; i < characterList.size(); i++) {
            indexByChar.put(characterList.get(i), i);
        }
    }


    public String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        int size = characterList.size();
        String lower = text.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            char originalSymbol = lower.charAt(i);
            Integer index = indexByChar.get(originalSymbol);
            if (index == null) {
                continue;
            }
            int newIndex = (index + shift + size) % size;
            result.append(characterList.get(newIndex));
        }
        return result.toString();
    }

    public String decrypt(String encryptedText, int shift) throws CipherException {
        StringBuilder result = new StringBuilder();
        int size = characterList.size();
        String lower = encryptedText.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            char encryptedSymbol = lower.charAt(i);
            Integer index = indexByChar.get(encryptedSymbol);
            if (index == null) {
                throw new CipherException(
                        "Символа '" + encryptedSymbol + "' нет в алфавите. Невозможно расшифровать текст.");
            }
            int newIndex = (index - shift + size) % size;
            result.append(characterList.get(newIndex));
        }
        return result.toString();
    }
}
