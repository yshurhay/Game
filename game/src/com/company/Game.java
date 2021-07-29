package com.company;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;


public class Game {
    private static final String HMAC_ALGO = "HmacSHA512";

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length*2);
        for(byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Scanner in = new Scanner(System.in);
        String error = "Input error. Parameters count should be >= 3 and odd. Strings should not repeated.\n"+
                "Correct input: rock paper scissors lizard Spock";

        int count = args.length;
        String[] choices = new String[count];
        int computerChoice = 1 + (int)(Math.random() * choices.length);


        for (int i = 0; i < count; i++) {
            choices[i] = args[i];
            for (int j = 0; j < i; j++) {
                if (choices[i].equalsIgnoreCase(choices[j])){
                    System.out.println(error);
                    return;
                }
            }
        }

        if(count < 3 || count % 2 == 0){
            System.out.println(error);
            return;
        }



        SecureRandom secureRandom = new SecureRandom();
        byte[] HMAC = new byte[16];
        secureRandom.nextBytes(HMAC);

        Mac signer = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(HMAC, HMAC_ALGO);
        signer.init(keySpec);

        byte[] digest = signer.doFinal(Integer.toString(computerChoice).getBytes("utf-8"));
        System.out.println("HMAC: " + bytesToHex(digest));


        boolean a = true;
        int x = -1;

        while(a){
            for(int i = 0; i < count; i++){
                System.out.println((i+1 + " - " + choices[i]));
            }
            System.out.println("0 - Exit");
            System.out.println("Enter your move: ");
            if(in.hasNextInt())
                x = in.nextInt();
            else
                in.next();
            if(x == 0){
                return;
            } else if(x < 0 || x >count){
                System.out.println("----------------");
            } else{
                a = false;
            }
        }

        System.out.println("Your choice: " + x);
        System.out.println("Computer choice: " + computerChoice);


        int half = choices.length / 2;
        boolean z = true;

        for(int i = 0; i < half; i++){
            if(computerChoice - 1 == (x + i) % count){
                z = false;
                break;
            }
        }

        if(computerChoice == x){
            System.out.println("Draw!");
        } else if(z){
            System.out.println("You win!");
        } else{
            System.out.println("You lose!");
        }

        System.out.println("HMAC key: " + bytesToHex(HMAC));

        in.close();
    }



}