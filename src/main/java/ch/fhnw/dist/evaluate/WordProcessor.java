package ch.fhnw.dist.evaluate;


import java.util.HashMap;
import java.util.Map;

public class WordProcessor {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Word> processContent(){

        Map<String, Word> wordlist = new HashMap<>();

        String[] splittedContent = this.content.split(" ");

        for( String word: splittedContent ){
            Word listW = new Word(0,0,0.0,0.0);
            wordlist.put(word, listW);
        }

        return wordlist ;
    }
}
