/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWord;
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();
        wordLength = DEFAULT_WORD_LENGTH;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = getSortedWord(word);
            //wordList.add(word);
            wordSet.add(word);
            if(!lettersToWord.containsKey(sortedWord))
                lettersToWord.put(sortedWord,new ArrayList<String>());
            lettersToWord.get(sortedWord).add(word);

            if(!sizeToWord.containsKey(word.length()))
                sizeToWord.put(word.length(),new ArrayList<String>());
            sizeToWord.get(word.length()).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    /*public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String word:wordList){
            if(word.length() == targetWord.length() &&
                    getSortedWord(word).equals(getSortedWord(targetWord))){
                result.add(word);
            }
        }
        return result;
    }*/

    private String getSortedWord(String word){
        char[] word_array = word.toCharArray();
        Arrays.sort(word_array);
        return new String(word_array);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char a='a';a <= 'z';a++){
            String key = getSortedWord(word.concat(Character.toString(a)));
            if(lettersToWord.containsKey(key)){
                for(String out:lettersToWord.get(key)){
                    if(isGoodWord(out,word))
                        result.add(out);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> sizeWordList =  sizeToWord.get(wordLength);
        int index = random.nextInt(sizeWordList.size());
        int initialIndex = index;
        String word;
        do{
            word = sizeWordList.get(index);
            index++;
            index%=sizeWordList.size();
            if(index == initialIndex){
                sizeWordList = sizeToWord.get(++wordLength);
                index = random.nextInt(sizeWordList.size());
                initialIndex = index;
            }
        }while(getAnagramsWithOneMoreLetter(word).size()<MIN_NUM_ANAGRAMS);
        wordLength = Math.min(MAX_WORD_LENGTH,wordLength+1);
        return word;
    }

    public void decrementWordLength(){
        wordLength--;
    }
}
