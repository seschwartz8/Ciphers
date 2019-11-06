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
    
    public HashSet<String> readDictionary(FileResource fr) {
        // SUMMARY: Adds each line of file to a "dictionary" of lowercase words to be referenced as "real words"
        HashSet<String> dictionary = new HashSet<String>();
        // Read each line
        for (String line : fr.lines()) {
            dictionary.add(line.toLowerCase());
        }
        return dictionary;
    }
    
    public int countRealWords(String message, HashSet<String> dictionary) {
        // Splits a message into words and determine how many of them are "real words"
        String[] splitWords = message.split("//W");
        int wordCount = 0;
        for (String word : splitWords) { // If word is in dictionary, count it
            if (dictionary.contains(word.toLowerCase())){
                wordCount ++;
            }
        }
        return wordCount;
    }
    
    public String breakForLanguage (String encrypted, HashSet<String> dictionary) {
        // Tries key lengths to determine best key based on greatest number of "real words" in decrypted message
        int highestWordCount = 0;
        String decryptedMessage = "";
        for (int length = 1; length <= encrypted.length(); length ++){
            // Get potential key, for this trial key length
            int[] key = tryKeyLength(encrypted, length, 'e');
            // Use key to decrypt the message
            VigenereCipher vigenere = new VigenereCipher(key);
            String decrypted = vigenere.decrypt(encrypted);
            // Count how many "real words" the decrypted message contains, based on user-selected dictionary
            int wordCount = countRealWords(decrypted, dictionary);
            // Keep track of key with most "real words"
            if (wordCount > highestWordCount){
                decryptedMessage = decrypted;
            }
        }
        return decryptedMessage;
    }

    public void breakVigenere () {
        // SUMMARY: Uses breakVigenere methods to provide information to VigenereCipher class and allow message decryption
        // Select a file to decrypt
        FileResource fr = new FileResource();
        String message = fr.asString();
        // Select dictionary of words to compare against
        FileResource fr2 = new FileResource();
        HashSet<String> dictionary = readDictionary(fr2);
        // Decrypt English message of unknown key length
        String decrypted = breakForLanguage(message, dictionary);
        System.out.println(decrypted); 
        // Should print "SCENE II. Athens. QUINCE'S house..." for "athens_keyflute.txt" and "English" dictionary
    }
    
}
