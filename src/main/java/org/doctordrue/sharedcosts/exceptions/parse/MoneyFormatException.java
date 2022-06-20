package org.doctordrue.sharedcosts.exceptions.parse;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class MoneyFormatException extends BaseException {

   public MoneyFormatException() {
      super(ParseError.MONEY_AMOUNT_PARSE_ERROR);
   }
}
