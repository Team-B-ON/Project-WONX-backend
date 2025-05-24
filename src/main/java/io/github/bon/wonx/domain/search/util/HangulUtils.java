package io.github.bon.wonx.domain.search.util;

public class HangulUtils {
  private static final char HANGUL_BASE = 0xAC00;
  private static final char CHOSEONG_BASE = 588;
  private static final char JUNGSEONG_BASE = 28;

  private static final char[] CHOSEONG = {
      'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
      'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
  };

  private static final char[] JUNGSEONG = {
      'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
      'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
  };

  private static final char[] JONGSEONG = {
      ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
      'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
      'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
  };

  public static String disassemble(String str) {
    StringBuilder result = new StringBuilder();
    for (char ch : str.toCharArray()) {
      if (ch >= 0xAC00 && ch <= 0xD7A3) {
        int code = ch - HANGUL_BASE;
        int cho = code / CHOSEONG_BASE;
        int jung = (code % CHOSEONG_BASE) / JUNGSEONG_BASE;
        int jong = code % JUNGSEONG_BASE;

        result.append(CHOSEONG[cho]);
        result.append(JUNGSEONG[jung]);
        if (jong != 0)
          result.append(JONGSEONG[jong]);
      } else {
        result.append(ch);
      }
    }
    return result.toString();
  }

  public static String extractChosung(String str) {
    StringBuilder result = new StringBuilder();
    for (char ch : str.toCharArray()) {
      if (ch >= 0xAC00 && ch <= 0xD7A3) {
        int code = ch - 0xAC00;
        int cho = code / 588;
        result.append(CHOSEONG[cho]);
      } else if (Character.isLetter(ch)) {
        result.append(ch); // 영어 등은 그대로 추가
      }
    }
    return result.toString();
  }
}
