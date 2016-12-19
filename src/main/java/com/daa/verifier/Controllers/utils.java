package com.daa.verifier.Controllers;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Random;
/**
 * Created by DK on 12/3/16.
 */
public class utils {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String generateSessionId() {
        Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        Long rand = new Random().nextLong();
        String value = String.valueOf(timestamp)+String.valueOf(rand);
        System.out.println("sessionId String: "+value);
        System.out.println("sessionId hashString: "+hashString(value));
        return hashString(value);
    }

    public static String hashString(String input){
        if(input == null){
            return null;
        }
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] h = digest.digest(input.getBytes());
            return bytesToHex(h);
        }catch(Exception e){
            System.out.println("MD5" + e.getMessage());
            return null;
        }

    }
}
