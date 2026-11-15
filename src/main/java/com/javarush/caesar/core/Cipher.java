package com.javarush.caesar.core;
import com.javarush.caesar.exception.CipherException;
import java.util.List;

public class Cipher {

    private final List<Character> characterList;

    public Cipher(List<Character> characterList) {
        this.characterList = characterList;
    }


    public String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        int size = characterList.size();
        String lower = text.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            char originalSymbol = lower.charAt(i);
            if (!characterList.contains(originalSymbol)) {
                result.append(originalSymbol);
                continue;
            }
            int indexOfOriginalSymbol = characterList.indexOf(originalSymbol);
            int newIndex = (indexOfOriginalSymbol + shift) % size;
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
            if (!characterList.contains(encryptedSymbol)) {
                throw new CipherException("Символа " + encryptedSymbol + " нет в алфавите. Невозможно расшифровать текст.");
            }
            int indexOfEncryptedSymbol = characterList.indexOf(encryptedSymbol);
            int newIndex = (indexOfEncryptedSymbol - shift + size) % size;
            result.append(characterList.get(newIndex));
        }
        return result.toString();
    }
}
