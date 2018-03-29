package com.logsentinel;

import java.util.List;

/**
 * Implement this to extract keywords from given text
 */
public interface EncryptingKeywordExtractor {

    List<String> extract(String text);
}
