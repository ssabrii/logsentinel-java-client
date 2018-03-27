package com.logsentinel;

import java.util.List;

public interface EncryptingKeywordExtractor {

    List<String> extract(String text);
}
