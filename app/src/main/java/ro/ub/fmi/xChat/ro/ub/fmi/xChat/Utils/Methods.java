package ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils;

import java.util.Random;

public class Methods {

    public static String randomString(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int length = 20;
        char tempChar;
        for(int i=0; i<length; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return  randomStringBuilder.toString();
    }

}
