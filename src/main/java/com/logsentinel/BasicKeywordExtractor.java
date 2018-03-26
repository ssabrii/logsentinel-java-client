package com.logsentinel;

import com.logsentinel.util.EncryptUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class BasicKeywordExtractor implements KeywordsExtractor {

    private static Analyzer analyzer = new StandardAnalyzer();

    private byte[] encryptionKey;

    public BasicKeywordExtractor(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @Override
    public List<String> extract(Object obj) {
        String text = obj.toString();
        List result = new ArrayList<>();

        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(text));
            stream.reset();
            while (stream.incrementToken()) {
                result.add(encrypt(stream.getAttribute(CharTermAttribute.class).toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String encrypt(String keyWord) {
        if (encryptionKey == null) {
            return keyWord;
        }
        try {
            return EncryptUtil.encrypt(keyWord, encryptionKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform keyword encryption", e);
        }
    }
}
