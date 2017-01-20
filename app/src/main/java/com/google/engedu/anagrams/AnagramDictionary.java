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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    ArrayList<String> wordList = new ArrayList<>();
    HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    HashSet<String> wordSet = new HashSet<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sorted;
            sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted)){
                lettersToWord.get(sorted).add(word);
            }
            else {
                ArrayList<String> newList = new ArrayList<>();
                newList.add(word);
                lettersToWord.put(sorted, newList);
            }
        }


    }

    public String sortLetters(String string){
        char [] strToChar = string.toCharArray();
        for(int i=0;i<strToChar.length-1;i++){
            if(strToChar[i] > strToChar[i+1]){
                char temp = strToChar[i];
                strToChar[i] = strToChar[i+1];
                strToChar[i+1] = temp;
            }
        }
        return String.valueOf(strToChar);
    }

    public boolean isGoodWord(String word, String base) {
        if((wordSet.contains(word)) && (!word.contains(base)))
            return true;
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        result = lettersToWord.get(sortLetters(targetWord));
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        String newString ;
        char ch = 'a';
        for(int i = 0 ; i < 26; i++){
            newString = word;
            char newCh = (char)(ch + i);
            newString = newString.concat(String.valueOf(newCh));
            newString = sortLetters(newString);
            if(lettersToWord.containsKey(newString))
            result.addAll(lettersToWord.get(newString));
        }
        for(int i=0;i<result.size();i++){
            if(!isGoodWord(result.get(i), word))
                result.remove(i);
        }

        return result;
    }

    public String pickGoodStarterWord() {
        String randWord;
        while (true) {
            int randNum = random.nextInt(wordList.size());
            randWord = wordList.get(randNum);
            if((randWord.length()<=MAX_WORD_LENGTH && randWord.length()>= DEFAULT_WORD_LENGTH) && (getAnagramsWithOneMoreLetter(randWord).size()>=MIN_NUM_ANAGRAMS))
                break;
        }
        return randWord;
    }
}
