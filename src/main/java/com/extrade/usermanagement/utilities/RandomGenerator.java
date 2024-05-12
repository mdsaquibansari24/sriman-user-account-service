package com.extrade.usermanagement.utilities;

import java.security.SecureRandom;

public class RandomGenerator {
    private static final String[] ALPHA_NUMERIC_SEQUENCE=new String[]{"C", "1", "B", "a", "D", "9", "d", "E", "0", "@", "X", "e", "z", "w", "b", "W", "4", "p", "Q", "P", "q", "s", "T", "S", "8", "t", "k", "2", "1", "3", "v"};
    private static final String[] NUMBER_SEQUENCE=new String[]{"2","8","9","1","4","6","3","7","5","0"};

    public static String mobileNoVerificationOtpGenerator(final int length){

       final StringBuilder sb=new StringBuilder();
        final SecureRandom random=new SecureRandom();
        for(int i=0;i<length;i++){
            sb.append(NUMBER_SEQUENCE[random.nextInt(10)]);

        }
        return sb.toString();
    }
    public static String emailAddressVerificationOtpGenerator(final int length){
        StringBuilder sb=new StringBuilder();
        SecureRandom random=new SecureRandom();
        for(int i=0;i<length;i++){
            sb.append(ALPHA_NUMERIC_SEQUENCE[random.nextInt(31)]);
        }
        return sb.toString();
    }


    public static void main(String[] args){
        System.out.println(mobileNoVerificationOtpGenerator(6));
        System.out.println(emailAddressVerificationOtpGenerator(6));
    }
}
