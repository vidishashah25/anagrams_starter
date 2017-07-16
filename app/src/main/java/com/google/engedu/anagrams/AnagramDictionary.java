package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = 3;

    private Random random = new Random();
    private ArrayList<String>wordList = new ArrayList<>();
    private HashSet<String>wordSet = new HashSet<>();
    private HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();


    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            ArrayList<String> temp1;

            String sortedWord = alphabeticalOrder(word);

            if(lettersToWord.containsKey(sortedWord)){
                temp1 = lettersToWord.get(sortedWord);
            }
            else {
                temp1 = new ArrayList<>();
            }
            temp1.add(word);

            lettersToWord.put(sortedWord,temp1);

            ArrayList<String> temp2;

            int length = word.length();


            if (sizeToWords.containsKey(length)){
                temp2 = sizeToWords.get(length);
            }
            else{
               temp2 = new ArrayList<>();
            }
            temp2.add(word);
            sizeToWords.put(length,temp2);


        }

        
    }

    public String alphabeticalOrder(String word){

        String returnWord;

        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        returnWord = new String(charArray);

        return returnWord;
    }

    public boolean isGoodWord(String word, String base) {

        if (wordSet.contains(word)&& !word.contains(base))
            return true;
        else
            return false;

    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        String sortedWord;
        for (char c = 'a';c <='z' ; c++ ){

            String newWord = word + c;
            sortedWord = alphabeticalOrder(newWord);

            if (lettersToWord.containsKey(sortedWord)){
                result.addAll(lettersToWord.get(sortedWord));
            }

        }

        for (int i = result.size()-1; i >=0 ; i--){
            String currentWord = result.get(i);

            if (!isGoodWord(currentWord,word)){
                result.remove(i);
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {

        String word = "";

        ArrayList<String>possibleWords = new ArrayList<>();

        if (wordLength <= MAX_WORD_LENGTH){
            possibleWords = sizeToWords.get(wordLength);
        }
        int i = random.nextInt(possibleWords.size());

        int j;

        for (j = i; j<possibleWords.size();j++){

            String currentWord = possibleWords.get(j);

            if (getAnagramsWithOneMoreLetter(currentWord).size() >= MIN_NUM_ANAGRAMS){
                word = currentWord;
                break;
            }
        }
        if ((j == possibleWords.size() -1) && word.length()<3){
            for (j = 0 ; j < i ; j++){
                String currentWord = possibleWords.get(j);

                if (getAnagramsWithOneMoreLetter(currentWord).size() >= MIN_NUM_ANAGRAMS){
                    word = currentWord;
                    break;
                }
            }
        }

        if (wordLength < MAX_WORD_LENGTH){
            wordLength ++;
        }
        else if(wordLength == MAX_WORD_LENGTH ){
            wordLength = 3;
        }

        return word;
    }
}
