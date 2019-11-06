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
        // SUMMARY: Returns the shifts for each index in the key of known length (and message of known language)
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
        //WRITE YOUR CODE HERE
    }
    
    
    public void testVigenere() {
        // Slice String
        String slicedResult2 = sliceString("abcdefghijklm", 4, 5); // should return "ej"
        String slicedResult1 = sliceString("abcdefghijklm", 0, 3); // should return "adgjm"
        String slicedResult3 = sliceString("1234567", 0, 3);
        System.out.println(slicedResult1);
        System.out.println(slicedResult2);
        System.out.println(slicedResult3);
        // tryKeyLength
        FileResource fr = new FileResource("./VigenereTestData/athens_keyflute.txt");
        String message = fr.asString();
        int[] key = tryKeyLength(message, 5, 'e');
        for (int ch : key) {
            // Should print 5, 11, 20, 19, 4
            System.out.println(ch);
        }
    }
}
