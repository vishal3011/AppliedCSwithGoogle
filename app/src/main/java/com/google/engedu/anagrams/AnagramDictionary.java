package com.google.engedu.anagrams;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static  int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    public ArrayList<String> wordList = new ArrayList<>();
    public HashSet<String> wordSet = new HashSet<>();
    public HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    public HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();


    public AnagramDictionary(InputStream wordListStream) throws IOException {


        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);

            ArrayList<String> temp_words1 = new ArrayList<String>();

            String sortWord = sortLetters(word);
            if(lettersToWord.containsKey(sortWord)){
                temp_words1 = lettersToWord.get(sortWord);
                temp_words1.add(word);
                lettersToWord.put(sortWord, temp_words1);
            }
            else{
                temp_words1.add(word);
                lettersToWord.put(sortWord,temp_words1);

            }
            ArrayList<String> temp_words2 = new ArrayList<>();
            int l = word.length();
            if(sizeToWord.containsKey(l)) {
            temp_words2 = sizeToWord.get(l);
                temp_words2.add(word);
                sizeToWord.put(l,temp_words2);

            }
            else{
                temp_words2.add(word);
                sizeToWord.put(l,temp_words2);
            }
            }
        }




    public boolean isGoodWord(String word, String base) {
    if(wordSet.contains(word) && !word.contains(base))
        return true;
        else
        return false;
    }

    public String sortLetters(String targetWord) {
        Character[] chars = new Character[targetWord.length()];
        for (int i = 0; i < chars.length; i++)
            chars[i] = targetWord.charAt(i);



        Arrays.sort(chars);

        StringBuilder sb = new StringBuilder(chars.length);
        for (char c : chars) sb.append(c);
        String sortedword = sb.toString();

        return sortedword;
    }



//    public ArrayList<String> getAnagrams(String targetWord) {
//        ArrayList<String> result = new ArrayList<String>();
//        String target = sortLetters(targetWord);
//
//        if(lettersToWord.containsKey(target)){
//            result = lettersToWord.get(target);
//        }
//        result.remove(targetWord);
////        for(int i = 0;i<wordList.size();i++){
////            String nw = wordList.get(i);
////            if(sortLetters(nw)== target){
////                result.add(nw);
////            }
//    return result;
//    }





    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String sw = sortLetters(word);
        String newWord;
        for(char alphabet = 'a'; alphabet<'z'; alphabet++){

            newWord = alphabet + sw;
            newWord = sortLetters(newWord);

            if(lettersToWord.containsKey(newWord)){
                result.addAll(lettersToWord.get(newWord));


        }
            for(int i = 0;i<result.size();i++) {
            //Log.d("AD list", result.get(i));
                if(!isGoodWord(result.get(i),word)){
                    //Log.d("AD removed", result.remove(i));
                    result.remove(i);
                }
            }
            }


        return result;
    }

    public String pickGoodStarterWord() {
        String word = new String();
        int j;
        ArrayList<String> lengthWords = new ArrayList<>();
        if(wordLength <= MAX_WORD_LENGTH){
            lengthWords = sizeToWord.get(wordLength);

        }

        int i = random.nextInt(lengthWords.size());
        for(j = i; j < lengthWords.size(); j++){
            if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size() >= MIN_NUM_ANAGRAMS){
                word = lengthWords.get(j);
                break;
            }
        }

        if(j == lengthWords.size()-1 && word == null){
            for(j=0;j<i;j++){
                if (getAnagramsWithOneMoreLetter(lengthWords.get(j)).size() >= MIN_NUM_ANAGRAMS){
                    word = lengthWords.get(j);
                    break;

                }
            }
        }
        if(wordLength <= MAX_WORD_LENGTH)
            wordLength++;
        else
            wordLength = 3;

        return word;
    }
}
