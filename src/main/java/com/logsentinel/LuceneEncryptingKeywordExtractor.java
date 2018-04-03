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

import static com.logsentinel.util.EncryptUtil.base64Encode;
import static com.logsentinel.util.EncryptUtil.hash;
import static tests.TestClient.printByteaerray;

/**
 * Extracts keywords from given text and encrypts them.
 * Uses Lucene StandardAnalyzer to tokenize the string
 */
public class LuceneEncryptingKeywordExtractor implements EncryptingKeywordExtractor {

    private static Analyzer analyzer = new StandardAnalyzer();

    private byte[] encryptionKey;

    public LuceneEncryptingKeywordExtractor(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @Override
    public List<String> extract(String text) {
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
            System.out.println(keyWord);
            byte[] encrypted = EncryptUtil.encrypt(keyWord, encryptionKey, false);
            byte[] hashed = hash(encrypted);
            return base64Encode(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform keyword encryption", e);
        }
    }
}
