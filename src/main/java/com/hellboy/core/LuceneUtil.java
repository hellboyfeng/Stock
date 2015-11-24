package com.hellboy.core;

import com.google.common.collect.Lists;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by hellboy on 2015/7/29.
 */
public class LuceneUtil {
    public static List<String> split(String value) throws IOException {
        List<String> results = Lists.newArrayList();
        Analyzer analyzer = new StandardAnalyzer();
        //使用分词器处理测试字符串
        StringReader reader = new StringReader(value);
        TokenStream tokenStream = analyzer.tokenStream("", reader);
        tokenStream.reset();
        CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
        int l = 0;
        //输出分词器和处理结果
        while (tokenStream.incrementToken()) {
            results.add(term.toString());
            l += term.toString().length();
        }
        return results;
    }
}
