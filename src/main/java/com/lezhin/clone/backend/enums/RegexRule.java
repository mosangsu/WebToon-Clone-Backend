package com.lezhin.clone.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum RegexRule {

  가("^[가-깋까-낗ㄱㄲ]"),
  나("^[나-닣ㄴ]"),
  다("^[다-딯따-띻ㄷㄸ]"),
  라("^[라-맇ㄹ]"),
  마("^[마-밓ㅁ]"),
  바("^[바-빟빠-삫ㅂㅃ]"),
  사("^[사-싷싸-앃ㅅㅆ]"),
  아("^[아-잏ㅇ]"),
  자("^[자-짛짜-찧ㅈㅉ]"),
  차("^[차-칳ㅊ]"),
  카("^[카-킿ㅋ]"),
  타("^[타-팋ㅌ]"),
  파("^[파-핗ㅍ]"),
  하("^[하-힣ㅎ]"),
  기타("^[^가-힣ㄱ-ㅎ]"),
  UNKNOWN("");
  
  private String regex;

  RegexRule(String regex) {
    this.regex = regex;
  }

  public String getName() {
    return name();
  }

  public String getRegex() {
      return this.regex;
  }
    
  public static RegexRule find(String description) {
      return Arrays.stream(values()).filter(regexRule -> regexRule.getName().equals(description)).findAny().orElse(UNKNOWN);
  }
}