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

package com.google.engedu.ghost;

import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private ArrayList<String> even;
    private ArrayList<String> odd;
    private List<String> restrictedWords;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        even = new ArrayList<>();
        odd = new ArrayList<>();
        restrictedWords = words;
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
            if(word.length()%2==0)
                even.add(word);
            else
                odd.add(word);
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    public void reset(){
     restrictedWords = words;
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix == null){
            Random r = new Random();
            int index = r.nextInt(words.size());
            return words.get(index);
        }else{
            return binarySearch(words,prefix,0,words.size()-1);
        }
    }

    private String binarySearch(ArrayList<String> list,String prefix, int start, int end){
        if(start<=end){
            int middle = (end+start)/2;
            if(list.get(middle).startsWith(prefix)){
                return list.get(middle);
            }else{
                if(list.get(middle).compareTo(prefix) < 0){
                    return binarySearch(list,prefix,middle+1,end);
                }else{
                    return binarySearch(list,prefix,start,middle-1);
                }
            }
        }else{
            return null;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix == null){
            Random r = new Random();
            int index = r.nextInt(restrictedWords.size());
            return restrictedWords.get(index);
        }
        int prefixLength = prefix.length();
        int start = 0;
        int end = restrictedWords.size()-1;
        int middle;
        int foundAt = -1;
        while(start<=end){
            middle = (end+start)/2;
            if(start>end){
                return null;
            }else if(restrictedWords.get(middle).startsWith(prefix)){
                foundAt=middle;
                break;
            }else if(restrictedWords.get(middle).compareTo(prefix)<0){
                start = middle+1;
            }else{
                end = middle-1;
            }
        }
        if(foundAt==-1)
            return null;
        start = foundAt - 1;
        end = foundAt + 1;
        even = new ArrayList<>();
        odd = new ArrayList<>();
        String word;
        while(start>=0 && restrictedWords.get(start).startsWith(prefix)){
            word = restrictedWords.get(start);
            if(word.length()%2==0)
                even.add(word);
            else
                odd.add(word);
            start--;

        }
        while(end<restrictedWords.size() && restrictedWords.get(end).startsWith(prefix)){
            word = restrictedWords.get(end);
            if(word.length()%2==0)
                even.add(word);
            else
                odd.add(word);
            end++;
        }
        start++;
        restrictedWords = restrictedWords.subList(start,end);
        Random r = new Random();
        if(prefixLength % 2 == 0){
            if(even.size()>0)
                return even.get(r.nextInt(even.size()));
            else
                return restrictedWords.get(r.nextInt(restrictedWords.size()));
        }else{
            if(odd.size()>0)
                return odd.get(r.nextInt(odd.size()));
            else
                return restrictedWords.get(r.nextInt(restrictedWords.size()));
        }
    }
}
