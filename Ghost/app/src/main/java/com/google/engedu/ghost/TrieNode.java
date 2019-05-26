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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }





















// RECURSIVE
    public void add(String s) {
        if(s.length()==0){
            isWord=true;
        }else{
            String key= Character.toString(s.charAt(0));
            if(!children.containsKey(key))
                children.put(key,new TrieNode());
            children.get(key).add(s.substring(1));
        }
    }





















    public boolean isWord(String s) {
        if(s.length()==0){
            return isWord;
        }else{
            String key = Character.toString(s.charAt(0));
            return children.containsKey(key) &&
                  children.get(key).isWord(s.substring(1));
        }
    }
































    public String getAnyWordStartingWith(String s) {
        if(s == null || s.length()==0 ){
            List<String> keyList = new ArrayList<String>(children.keySet());
            Random r =new Random();
            int choice  = r.nextInt(keyList.size());
            return keyList.get(choice);
        }else{
            String key = Character.toString(s.charAt(0));
            if(!children.containsKey(key))
                return null;
            return children.get(key).
                    getAnyWordStartingWith(s.substring(1));
        }
    }















    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
