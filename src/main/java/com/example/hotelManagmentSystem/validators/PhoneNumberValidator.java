package com.example.hotelManagmentSystem.validators;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    public static boolean validate(String phoneNumber){

//          \+?: Matches an optional + for the country code.
//          \d{1,4}?: Matches 1 to 4 digits for the country code.
//          [-.\s]?: Matches an optional separator (hyphen, dot, or space).
//          (\(?\d{1,3}?\)?[-.\s]?)?: Matches an optional area code with optional parentheses and separator.
//          (\d[-.\s]?){6,14}\d: Matches 6 to 14 digits with optional separators in between,
//          ensuring the total length is reasonable for international numbers.
//
        String regex = "\\+?\\d{1,4}?[-.\\s]?(\\(?\\d{1,3}?\\)?[-.\\s]?)?(\\d[-.\\s]?){6,14}\\d";
        phoneNumber = phoneNumber.replaceAll("[^\\d]", "");

        return  Pattern.compile(regex)
                .matcher(phoneNumber)
                .matches();

    }
}
