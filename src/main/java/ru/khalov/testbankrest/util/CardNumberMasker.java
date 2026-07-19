package ru.khalov.testbankrest.util;

import org.springframework.stereotype.Component;

@Component
public class CardNumberMasker {

    public static String mask(String lastFourDigits){
        return "**** **** **** " + lastFourDigits;
    }

}
