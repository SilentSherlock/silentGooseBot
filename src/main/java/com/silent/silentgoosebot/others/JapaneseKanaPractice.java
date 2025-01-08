package com.silent.silentgoosebot.others;

import java.util.*;

public class JapaneseKanaPractice {

    // 假名与罗马音映射表
    private static final Map<String, String> hiraMap = new LinkedHashMap<>();
    private static final Map<String, String> kataMap = new LinkedHashMap<>();
    private static final Random random = new Random();
    private static final List<String> errorKanaList = new ArrayList<>(); // 存储用户输入错误的假名

    static {
        // 平假名映射
        String[][] hiraArray = {
                {"あ", "a"}, {"い", "i"}, {"う", "u"}, {"え", "e"}, {"お", "o"},
                {"か", "ka"}, {"き", "ki"}, {"く", "ku"}, {"け", "ke"}, {"こ", "ko"},
                {"さ", "sa"}, {"し", "shi"}, {"す", "su"}, {"せ", "se"}, {"そ", "so"},
                {"た", "ta"}, {"ち", "chi"}, {"つ", "tsu"}, {"て", "te"}, {"と", "to"},
                {"な", "na"}, {"に", "ni"}, {"ぬ", "nu"}, {"ね", "ne"}, {"の", "no"},
                {"は", "ha"}, {"ひ", "hi"}, {"ふ", "fu"}, {"へ", "he"}, {"ほ", "ho"},
                {"ま", "ma"}, {"み", "mi"}, {"む", "mu"}, {"め", "me"}, {"も", "mo"},
                {"や", "ya"}, {"ゆ", "yu"}, {"よ", "yo"},
                {"ら", "ra"}, {"り", "ri"}, {"る", "ru"}, {"れ", "re"}, {"ろ", "ro"},
                {"わ", "wa"}, {"を", "wo"}, {"ん", "n"}
        };

        // 片假名映射
        String[][] kataArray = {
                {"ア", "a"}, {"イ", "i"}, {"ウ", "u"}, {"エ", "e"}, {"オ", "o"},
                {"カ", "ka"}, {"キ", "ki"}, {"ク", "ku"}, {"ケ", "ke"}, {"コ", "ko"},
                {"サ", "sa"}, {"シ", "shi"}, {"ス", "su"}, {"セ", "se"}, {"ソ", "so"},
                {"タ", "ta"}, {"チ", "chi"}, {"ツ", "tsu"}, {"テ", "te"}, {"ト", "to"},
                {"ナ", "na"}, {"ニ", "ni"}, {"ヌ", "nu"}, {"ネ", "ne"}, {"ノ", "no"},
                {"ハ", "ha"}, {"ヒ", "hi"}, {"フ", "fu"}, {"ヘ", "he"}, {"ホ", "ho"},
                {"マ", "ma"}, {"ミ", "mi"}, {"ム", "mu"}, {"メ", "me"}, {"モ", "mo"},
                {"ヤ", "ya"}, {"ユ", "yu"}, {"ヨ", "yo"},
                {"ラ", "ra"}, {"リ", "ri"}, {"ル", "ru"}, {"レ", "re"}, {"ロ", "ro"},
                {"ワ", "wa"}, {"ヲ", "wo"}, {"ン", "n"}
        };

        for (String[] hira : hiraArray) {
            hiraMap.put(hira[0], hira[1]);
        }

        for (String[] kata : kataArray) {
            kataMap.put(kata[0], kata[1]);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎来到日语假名练习程序！");
        System.out.println("请选择练习模式：");
        System.out.println("1 - 平假名");
        System.out.println("2 - 片假名");
        System.out.println("3 - 混合");
        System.out.print("请输入选项: ");

        int mode = scanner.nextInt();
        scanner.nextLine(); // 处理换行符
        Map<String, String> kanaMap = getKanaMap(mode);

        if (kanaMap == null) {
            System.out.println("无效选项，程序结束。");
            return;
        }

        String[] kanaKeys = kanaMap.keySet().toArray(new String[0]);

        while (true) {
            // 随机选择一个假名
            String randomKana = getRandomKana(kanaKeys);
            String correctRomanji = kanaMap.get(randomKana);

            while (true) {
                System.out.println("假名: " + randomKana);
                System.out.print("请输入对应的罗马音: ");
                String userInput = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("感谢使用假名练习程序，再见！");
                    scanner.close();
                    return;
                }

                if (correctRomanji.equalsIgnoreCase(userInput)) {
                    System.out.println("正确！继续下一个假名。");
                    break;
                } else {
                    System.out.println("错误！请再试一次。");
                    errorKanaList.add(randomKana); // 记录错误的假名
                }
            }
        }
    }

    private static Map<String, String> getKanaMap(int mode) {
        switch (mode) {
            case 1:
                return hiraMap;
            case 2:
                return kataMap;
            case 3:
                Map<String, String> mixedMap = new HashMap<>();
                mixedMap.putAll(hiraMap);
                mixedMap.putAll(kataMap);
                return mixedMap;
            default:
                return null;
        }
    }

    private static String getRandomKana(String[] kanaKeys) {
        // 增加错误假名的出现概率
        if (!errorKanaList.isEmpty() && random.nextInt(4) == 0) { // 25%概率出错假名
            return errorKanaList.get(random.nextInt(errorKanaList.size()));
        }
        return kanaKeys[random.nextInt(kanaKeys.length)];
    }
}