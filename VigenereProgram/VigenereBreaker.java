import java.util.*;
import edu.duke.*;
/* WRITTEN BY SARAH SCHWARTZ, WITH HELPER CLASSES PROVIDED BY DUKE.
   PROGRAM DESIGNED TO BREAK A VIGENERE CIPHER */

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        // SUMMARY: Returns every Nth character of a message, where N is the whichSlice position in totalSlices
        StringBuilder result = new StringBuilder();
        for (int n = 0; (n + whichSlice) < message.length(); n += totalSlices){
            // Extract single character at the desired whichSlice shift
            String targetChar = new String();
            targetChar = message.substring((n + whichSlice) , (n + whichSlice + 1));
            result.append(targetChar);
        }
        return result.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        // SUMMARY: Returns the shifts for each index in the key given length of key (and known language)
        for (int index = 0; index < klength; index ++){
            // For each character of the key, create a slice of the message shifted by that character 
            String messageSlice = sliceString(encrypted, index, klength);
            // Get key shift for that slice, based on most common letter statistics
            CaesarCracker caesarDecrypt = new CaesarCracker();
            int sliceKey = caesarDecrypt.getKey(messageSlice);
            key[index] = sliceKey;
        }
        return key;
    }

    public void breakVigenere () {
        // SUMMARY: Uses breakVigenere methods to provide information to VigenereCipher class and allow message decryption
        // Select a file to decrypt - currently only works for "athens_keyflute.txt"
        FileResource fr = new FileResource();
        String message = fr.asString();
        // Find message's key, if length is known and language is english
        int[] key = tryKeyLength(message, 5, 'e');
        // Create VigenereCipher object, pass it key, and have it decrypt the message
        VigenereCipher vigenere = new VigenereCipher(key);
        String decrypted = vigenere.decrypt(message);
        System.out.println(decrypted);
    }
    
}
