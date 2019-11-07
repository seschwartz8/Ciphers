import java.util.*;
import java.io.File;
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
            // Create CaesarCracker and pass it the language's most common letter
            CaesarCracker caesarDecrypt = new CaesarCracker(mostCommon);
            // Get key shift for that slice, based on most common letter statistics
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
        String[] splitWords = message.split("\\W+");
        int wordCount = 0;
        for (int i = 0; i < splitWords.length; i ++) { // If word is in dictionary, count it
            if (dictionary.contains(splitWords[i].toLowerCase())){
                wordCount ++;
            }
        }
        return wordCount;
    }
    
    public String breakForLanguage (String encrypted, HashSet<String> dictionary) {
        // Tries key lengths 1-100 to determine best key based on greatest number of "real words" in decrypted message
        int highestWordCount = 0;
        String decryptedMessage = "";
        // Determine most common letter in dictionary
        Character commonChar = mostCommonCharIn(dictionary);
        // For all potential key lengths, decrypt message and count real words in it
        for (int length = 1; length <= 100; length ++){
            // Get potential key, for this trial key length
            int[] key = tryKeyLength(encrypted, length, commonChar);
            // Use key to decrypt the message
            VigenereCipher vigenere = new VigenereCipher(key);
            String decrypted = vigenere.decrypt(encrypted);
            // Count how many "real words" the decrypted message contains, based on user-selected dictionary
            int wordCount = countRealWords(decrypted, dictionary);
            // Keep track of key with most "real words"
            if (wordCount > highestWordCount){
                highestWordCount = wordCount;
                decryptedMessage = decrypted;
            }
        }
        return decryptedMessage;
    }
    
    public Character mostCommonCharIn (HashSet<String> dictionary) {
        // SUMMARY: Returns most common character in language that uses the English alphabet
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        CaesarCracker caesar = new CaesarCracker();
        int[] finalCounts = new int[26];
        // For every word in the dictionary
        for (String word : dictionary) {
            // Count frequency of each letter
            int[] counts = caesar.countLetters(word);
            // Add those counts to the final letter counts
            for (int i = 0; i < counts.length; i++) {
                finalCounts[i] = finalCounts[i] + counts[i];
            }
        }
        // Determine highest letter count and return that character
        int highestCount = 0;
        int mostIndex = 0;
        for (int i = 0; i < finalCounts.length; i++) {
            if (finalCounts[i] > highestCount) {
                highestCount = finalCounts[i];
                mostIndex = i;
            }
        }
        Character mostCommonChar = alphabet.charAt(mostIndex);
        return mostCommonChar;
    }
    
    public String breakForAllLangs (String encrypted, HashMap<String,HashSet<String>> languages){
        int mostWords = 0;
        String decryptedMessage = "";
        String targetLanguage = "";
        for (String language : languages.keySet()){
            // Get corresponding dictionary for each language
            HashSet<String> currentDictionary = languages.get(language);
            // Create potential decryption
            String currentDecrypted = breakForLanguage(encrypted, currentDictionary);
            // Count "real words" in decryption
            int currentWords = countRealWords(currentDecrypted, currentDictionary);
            if (currentWords > mostWords) {
                mostWords = currentWords;
                decryptedMessage = currentDecrypted;
                targetLanguage = language;
            }
        }
        System.out.println("Target Language: " + targetLanguage);
        return decryptedMessage;
    }

    public void breakVigenere () {
        // SUMMARY: Uses breakVigenere methods to provide information to VigenereCipher class and allow message decryption
        // Select a file to decrypt
        FileResource fr = new FileResource();
        String message = fr.asString();
        // Map possible dictionary languages to their contents for later comparison
        DirectoryResource dr = new DirectoryResource();
        HashMap<String,HashSet<String>> dictionaries = new HashMap<String,HashSet<String>>();
        for (File f : dr.selectedFiles()){
            // Save name of language (aka name of file)
            String language = f.getName();
            // Notify the user that the dictionary is being processed
            System.out.println("Processing dictionaries...");
            // Create a file resource of the current file to be used by readDictionary
            FileResource fr2 = new FileResource(f);
            HashSet<String> dictionary = readDictionary(fr2);
            // Add language and its words to the map of dictionaries
            dictionaries.put(language, dictionary);
        }
        // Decrypt English message of unknown key length
        String decrypted = breakForAllLangs(message, dictionaries);
        System.out.println("DECRYPTED MESSAGE: " + decrypted);
    }
    
}
