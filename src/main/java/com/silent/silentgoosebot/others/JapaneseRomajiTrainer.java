package com.silent.silentgoosebot.others;

import java.util.*;

public class JapaneseRomajiTrainer {

    private static final Map<String, String> hiragana = new HashMap<>();
    private static final Map<String, String> katakana = new HashMap<>();

    static {
        // Hiragana mappings
        hiragana.put("a", "あ"); hiragana.put("i", "い"); hiragana.put("u", "う");
        hiragana.put("e", "え"); hiragana.put("o", "お");
        hiragana.put("ka", "か"); hiragana.put("ki", "き"); hiragana.put("ku", "く");
        hiragana.put("ke", "け"); hiragana.put("ko", "こ");
        hiragana.put("sa", "さ"); hiragana.put("shi", "し"); hiragana.put("su", "す");
        hiragana.put("se", "せ"); hiragana.put("so", "そ");
        hiragana.put("ta", "た"); hiragana.put("chi", "ち"); hiragana.put("tsu", "つ");
        hiragana.put("te", "て"); hiragana.put("to", "と");
        hiragana.put("na", "な"); hiragana.put("ni", "に"); hiragana.put("nu", "ぬ");
        hiragana.put("ne", "ね"); hiragana.put("no", "の");
        hiragana.put("ha", "は"); hiragana.put("hi", "ひ"); hiragana.put("fu", "ふ");
        hiragana.put("he", "へ"); hiragana.put("ho", "ほ");
        hiragana.put("ma", "ま"); hiragana.put("mi", "み"); hiragana.put("mu", "む");
        hiragana.put("me", "め"); hiragana.put("mo", "も");
        hiragana.put("ya", "や"); hiragana.put("yu", "ゆ"); hiragana.put("yo", "よ");
        hiragana.put("ra", "ら"); hiragana.put("ri", "り"); hiragana.put("ru", "る");
        hiragana.put("re", "れ"); hiragana.put("ro", "ろ");
        hiragana.put("wa", "わ"); hiragana.put("wo", "を"); hiragana.put("n", "ん");

        // B sounds
        hiragana.put("ba", "ば"); hiragana.put("bi", "び"); hiragana.put("bu", "ぶ");
        hiragana.put("be", "べ"); hiragana.put("bo", "ぼ");

        // P sounds
        hiragana.put("pa", "ぱ"); hiragana.put("pi", "ぴ"); hiragana.put("pu", "ぷ");
        hiragana.put("pe", "ぺ"); hiragana.put("po", "ぽ");

        // Dakuon (voiced sounds)
        hiragana.put("ga", "が"); hiragana.put("gi", "ぎ"); hiragana.put("gu", "ぐ");
        hiragana.put("ge", "げ"); hiragana.put("go", "ご");
        hiragana.put("za", "ざ"); hiragana.put("ji", "じ"); hiragana.put("zu", "ず");
        hiragana.put("ze", "ぜ"); hiragana.put("zo", "ぞ");
        hiragana.put("da", "だ"); hiragana.put("di", "ぢ"); hiragana.put("du", "づ");
        hiragana.put("de", "で"); hiragana.put("do", "ど");

        // Katakana mappings
        katakana.put("a", "ア"); katakana.put("i", "イ"); katakana.put("u", "ウ");
        katakana.put("e", "エ"); katakana.put("o", "オ");
        katakana.put("ka", "カ"); katakana.put("ki", "キ"); katakana.put("ku", "ク");
        katakana.put("ke", "ケ"); katakana.put("ko", "コ");
        katakana.put("sa", "サ"); katakana.put("shi", "シ"); katakana.put("su", "ス");
        katakana.put("se", "セ"); katakana.put("so", "ソ");
        katakana.put("ta", "タ"); katakana.put("chi", "チ"); katakana.put("tsu", "ツ");
        katakana.put("te", "テ"); katakana.put("to", "ト");
        katakana.put("na", "ナ"); katakana.put("ni", "ニ"); katakana.put("nu", "ヌ");
        katakana.put("ne", "ネ"); katakana.put("no", "ノ");
        katakana.put("ha", "ハ"); katakana.put("hi", "ヒ"); katakana.put("fu", "フ");
        katakana.put("he", "ヘ"); katakana.put("ho", "ホ");
        katakana.put("ma", "マ"); katakana.put("mi", "ミ"); katakana.put("mu", "ム");
        katakana.put("me", "メ"); katakana.put("mo", "モ");
        katakana.put("ya", "ヤ"); katakana.put("yu", "ユ"); katakana.put("yo", "ヨ");
        katakana.put("ra", "ラ"); katakana.put("ri", "リ"); katakana.put("ru", "ル");
        katakana.put("re", "レ"); katakana.put("ro", "ロ");
        katakana.put("wa", "ワ"); katakana.put("wo", "ヲ"); katakana.put("n", "ン");

        // B sounds
        katakana.put("ba", "バ"); katakana.put("bi", "ビ"); katakana.put("bu", "ブ");
        katakana.put("be", "ベ"); katakana.put("bo", "ボ");

        // P sounds
        katakana.put("pa", "パ"); katakana.put("pi", "ピ"); katakana.put("pu", "プ");
        katakana.put("pe", "ペ"); katakana.put("po", "ポ");

        // Dakuon (voiced sounds)
        katakana.put("ga", "ガ"); katakana.put("gi", "ギ"); katakana.put("gu", "グ");
        katakana.put("ge", "ゲ"); katakana.put("go", "ゴ");
        katakana.put("za", "ザ"); katakana.put("ji", "ジ"); katakana.put("zu", "ズ");
        katakana.put("ze", "ゼ"); katakana.put("zo", "ゾ");
        katakana.put("da", "ダ"); katakana.put("di", "ヂ"); katakana.put("du", "ヅ");
        katakana.put("de", "デ"); katakana.put("do", "ド");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> errors = new ArrayList<>();

        System.out.println("Welcome to the Japanese Romaji Trainer!");
        System.out.println("Choose training mode: 1 for Hiragana, 2 for Katakana, 3 for Mixed");
        int mode = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Map<String, String> trainingMap = new HashMap<>();
        if (mode == 1) {
            trainingMap.putAll(hiragana);
            System.out.println("You chose Hiragana training.");
        } else if (mode == 2) {
            trainingMap.putAll(katakana);
            System.out.println("You chose Katakana training.");
        } else if (mode == 3) {
            trainingMap.putAll(hiragana);
            trainingMap.putAll(katakana);
            System.out.println("You chose Mixed training.");
        } else {
            System.out.println("Invalid choice. Exiting.");
            return;
        }

        Random random = new Random();
        List<String> keys = new ArrayList<>(trainingMap.keySet());

        while (true) {
            String romaji = keys.get(random.nextInt(keys.size()));

            while (true) {
                System.out.println("What is the Japanese character for: " + romaji + "? ");
                String answer = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(answer)) {
                    System.out.println("Exiting. You made mistakes on the following characters:");
                    for (String error : errors) {
                        System.out.println(error + " -> " + trainingMap.get(error));
                    }
                    return;
                }

                // Mixed mode expects both Hiragana and Katakana answers
                if (mode == 3) {
                    String[] expected = {hiragana.get(romaji), katakana.get(romaji)};
                    if (expected[0].equals(answer.split(" ")[0]) && expected[1].equals(answer.split(" ")[1])) {
                        System.out.println("Correct!");
                        break;
                    } else {
                        System.out.println("Incorrect. Try again!");
                        errors.add(romaji);
                    }
                } else {
                    // Single mode (Hiragana or Katakana)
                    if (trainingMap.get(romaji).equals(answer)) {
                        System.out.println("Correct!");
                        break;
                    } else {
                        System.out.println("Incorrect. Try again!");
                        System.out.println("提示: " + trainingMap.get(romaji));
                        errors.add(romaji);
                    }
                }
            }

        }

    }
}

