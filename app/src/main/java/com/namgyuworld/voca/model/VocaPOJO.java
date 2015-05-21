package com.namgyuworld.voca.model;

/**
 * Created by Daniel Park on 4/10/15.
 */
public class VocaPOJO {

    private String vocaWord;
    private String vocaContents;

    public VocaPOJO(String word, String contents){
        this.vocaWord = word;
        this.vocaContents = contents;
    }

    public String getVocaWord() {
        return vocaWord;
    }

    public void setVocaWord(String vocaWord) {
        this.vocaWord = vocaWord;
    }

    public String getVocaContents() {
        return vocaContents;
    }

    public void setVocaContents(String vocaContents) {
        this.vocaContents = vocaContents;
    }
}
