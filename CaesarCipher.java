import edu.duke.*;

public class CaesarCipher {
    public String encrypt(String input, int key) {
        //Make a StringBuilder with message (encrypted)
        StringBuilder encrypted = new StringBuilder(input);
        //Write down the alphabet
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //Compute the shifted alphabet
        String shiftedAlphabet = alphabet.substring(key)+
        alphabet.substring(0,key);
        //Count from 0 to < length of encrypted, (call it i)
        for(int i = 0; i < encrypted.length(); i++) {
            //Look at the ith character of encrypted (call it currChar)
            char currChar = encrypted.charAt(i);
            int idx = -1;
            if (Character.isUpperCase(currChar)) {
                //Find the index of currChar in the alphabet (call it idx)
                idx = alphabet.indexOf(currChar);
            } else {
                //Convert to uppercase then find the index of currChar in the alphabet (call it idx)
                int currCharUpper = Character.toUpperCase(currChar);
                idx = alphabet.indexOf(currCharUpper);
            }
            //If currChar is in the alphabet
            if(idx != -1){
                //Get the idxth character of shiftedAlphabet (newChar)
                char newChar = shiftedAlphabet.charAt(idx);
                //Turn to lowercase if needed
                if (Character.isLowerCase(currChar)) {
                    newChar = Character.toLowerCase(newChar);
                }
                //Replace the ith character of encrypted with newChar
                encrypted.setCharAt(i, newChar);
            }
            //Otherwise: do nothing
        }
        //Your answer is the String inside of encrypted
        return encrypted.toString();
    }
    
    public String encryptTwoKeys(String input, int key1, int key2) {
        //Make a StringBuilder with message (encryptedTwoKeys)
        StringBuilder encryptedTwoKeys = new StringBuilder(input);
        //For each char in input, determine if odd or even idx
        for (int k = 0; k < encryptedTwoKeys.length(); k++) {
            //Get current character at current index k
            char currChar = encryptedTwoKeys.charAt(k);
            //Change the current character to a string for use in encrypt()
            String currCharStr = Character.toString(currChar);
            if (k % 2 == 0){
                //If even index, encrypt() with key1, turn back to char, and add new character to answer
                String newCharStr = encrypt(currCharStr, key1);
                char newChar = newCharStr.charAt(0);
                encryptedTwoKeys.setCharAt(k, newChar);
            } else {
                //If odd index, encrypt() with key2, turn back to char, and add new character to answer
                String newCharStr = encrypt(currCharStr, key2);
                char newChar = newCharStr.charAt(0);
                encryptedTwoKeys.setCharAt(k, newChar);
            }
            
        }
        return encryptedTwoKeys.toString();
    }
    
    public void testCaesarTwoKeys() {
        int key1 = 1;
        int key2 = 3;
        FileResource fr = new FileResource();
        String message = fr.asString();
        String encryptedTwoKeys = encryptTwoKeys(message, key1, key2);
        System.out.println(encryptedTwoKeys);
        String decryptedTwoKeys = encryptTwoKeys(encryptedTwoKeys, 26-key1, 26-key2);
        System.out.println(decryptedTwoKeys);
    }
    
    public void testCaesar() {
        int key = 17;
        FileResource fr = new FileResource();
        String message = fr.asString();
        String encrypted = encrypt(message, key);
        System.out.println(encrypted);
        String decrypted = encrypt(encrypted, 26-key);
        System.out.println(decrypted);
    }
}

