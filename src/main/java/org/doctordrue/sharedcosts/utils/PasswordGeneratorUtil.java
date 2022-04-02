package org.doctordrue.sharedcosts.utils;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

/**
 * @author Andrey_Barantsev
 * 3/30/2022
 **/
public class PasswordGeneratorUtil {

   private static final int DEFAULT_LENGTH = 10;

   public static String generate(int length) {
      CharacterData numbersData = EnglishCharacterData.Digit;
      CharacterData upperCaseData = EnglishCharacterData.UpperCase;
      CharacterData lowerCaseData = EnglishCharacterData.LowerCase;

      CharacterRule numbersRule = new CharacterRule(numbersData);
      CharacterRule upperCaseRule = new CharacterRule(upperCaseData);
      CharacterRule lowerCaseRule = new CharacterRule(lowerCaseData);
      return new PasswordGenerator().generatePassword(length, numbersRule, upperCaseRule, lowerCaseRule);
   }

   public static String generate() {
      return generate(DEFAULT_LENGTH);
   }

}
