package com.javarush.caesar.core;
import com.javarush.caesar.exception.CipherException;
import java.util.List;

public class BruteForce {

    private static final String[] BIGRAMS = {
            "ст", "но", "то", "на", "ен", "ов", "ни", "ра", "во", "ко",
            "пр", "ро", "по", "ре", "ос", "го", "од", "ал", "ер", "ли",
            "ка", "та", "те", "ле", "де", "ла", "ло", "ли", "ит", "ат"
    };

    private static final String FREQUENT_LETTERS = "оеаинтсрвлкмдпуяыьгчзйхжшюцщэфъё";

    private static final String[] SHORT_WORDS = {
            "и", "в", "на", "не", "что", "как", "это", "по", "из", "от",
            "до", "за", "но", "да", "нет", "он", "она", "они", "мы", "вы",
            "я", "ты", "же", "ли", "бы"
    };

    public String decryptByBruteForce(String encryptedText, List<Character> alphabet) throws CipherException {
        if (encryptedText == null || encryptedText.isEmpty()
                || alphabet == null || alphabet.size() < 2) {
            return encryptedText;
        }

        Cipher cipher = new Cipher(alphabet);
        String best = encryptedText;
        int bestScore = Integer.MIN_VALUE;
        int bestKey = -1;

        for (int key = 0; key < alphabet.size(); key++) {
            String candidate;
            try {
                candidate = cipher.decrypt(encryptedText, key);
            } catch (CipherException e) {
                continue;
            }
            int score = score(candidate);
            if (score > bestScore) {
                bestScore = score;
                best = candidate;
                bestKey = key;
            }
        }
        return best;
    }

    private int score(String text) {
        if (text == null || text.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        int score = 0;
        String lower = text.toLowerCase();
        for (String bg : BIGRAMS) {
            int idx = 0;
            while ((idx = lower.indexOf(bg, idx)) >= 0) {
                score += 10;
                idx += bg.length();
            }
        }

        for (int i = 0; i < lower.length(); i++) {
            if (lower.charAt(i) == ' ') {
                score += 1;
            }
        }

        for (int i = 0; i < lower.length() - 1; i++) {
            char a = lower.charAt(i);
            char b = lower.charAt(i + 1);
            if (isPunctuation(a) && isPunctuation(b)) {
                score -= 5;
            }
        }

        for (int i = 0; i < lower.length() - 1; i++) {
            char c = lower.charAt(i);
            char next = lower.charAt(i + 1);
            if (isPunctuation(c) && next == ' ') {
                score += 5;
            }
        }

        String[] words = lower.split("[\\s.,!?«»\":;'()\\[\\]]+");
        for (String w : words) {
            if (w.isEmpty()) {
                continue;
            }
            int len = w.length();
            if (len >= 2 && len <= 10) {
                score += 2;
            } else if (len > 15) {
                score -= 3;
            }
            for (String sw : SHORT_WORDS) {
                if (sw.equals(w)) {
                    score += 8;
                    break;
                }
            }
        }

        for (int i = 0; i < lower.length(); i++) {
            if (FREQUENT_LETTERS.indexOf(lower.charAt(i)) >= 0) {
                score += 1;
            }
        }

        return score;
    }

    private boolean isPunctuation(char c) {
        return c == '.' || c == ',' || c == '!' || c == '?'
                || c == ':' || c == ';' || c == '«' || c == '»'
                || c == '"' || c == '\'';
    }
}