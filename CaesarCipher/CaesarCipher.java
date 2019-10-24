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
    
    public int[] countLetters (String message) {
        // SUMMARY: Counts occurrences of each letter of the alphabet in a string
        int [] counts = new int[26];
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int k = 0; k < message.length(); k++) {
            char ch = Character.toLowerCase(message.charAt(k));
            int index = alphabet.indexOf(ch);
            if (index != -1) { //If the character is a letter in the alphabet
                counts[index] += 1;
            }
        }
        return counts;
    }
    
    public int maxIndex(int[] counts) {
        // SUMMARY: Finds index with the highest count
        // Iterate through counts to get highest count and note its index
        int highest = 0;
        int index = 0;
        for (int i = 0; i < counts.length; i ++) {
            if (counts[i] > highest) {
                highest = counts[i];
                index = i;
            }
        }
        return index;
    }
    
    public String decrypt (String encrypted) {
        // SUMMARY: Decrypts string by finding the letter with highest frequency and assuming it's "E"
        int[] letterCount = countLetters(encrypted);
        int maxIdx = maxIndex(letterCount);
        // Calculate the shift, assuming the letter with maxIdx is "E", which should be at idx 4 with no shift
        int decryptKey = maxIdx - 4;
        if (maxIdx < 4) {
            decryptKey = 26 - (4 - maxIdx);
        }
        String decryptedMessage = encrypt(encrypted, 26-decryptKey);
        return decryptedMessage;
    }
    
    public void testCaesarEncrypt() {
        // One key
        int key = 17;
        FileResource fr = new FileResource();
        String message = fr.asString();
        String encrypted = encrypt(message, key);
        System.out.println(encrypted);
        String decrypted = encrypt(encrypted, 26-key);
        System.out.println(decrypted);
        // Two keys
        int key1 = 1;
        int key2 = 3;
        FileResource fr2 = new FileResource();
        String message2 = fr2.asString();
        String encryptedTwoKeys = encryptTwoKeys(message2, key1, key2);
        System.out.println(encryptedTwoKeys);
        String decryptedTwoKeys = encryptTwoKeys(encryptedTwoKeys, 26-key1, 26-key2);
        System.out.println(decryptedTwoKeys);
    }
    
    public void testCaesarDecrypt() {
        FileResource fr = new FileResource();
        String message = fr.asString();
        String encryptedMessage = encrypt(message, 4);
        System.out.println(encryptedMessage);
        String decrypted = decrypt(message);
        System.out.println(decrypted);
    }
}

