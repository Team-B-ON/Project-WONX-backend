package io.github.bon.wonx.domain.search.util;

public class HangulUtils {

    // 초성만 포함됐는지 여부 (e.g., ㅂㅈㅎ)
    public static boolean isChoseongOnly(String input) {
        return input.matches("^[ㄱ-ㅎ]+$");
    }

    // 초성을 한글 자모 정규식으로 변환 (e.g., ㅂㅈㅎ → ^[ㅂ][ㅈ][ㅎ])
    public static String choseongToRegex(String choseong) {
        StringBuilder regex = new StringBuilder("^");

        for (char c : choseong.toCharArray()) {
            regex.append("[").append(c).append("]");
        }

        return regex.toString();
    }
}
