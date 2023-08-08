package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSubstituter {
	private Map<String, String> map;
    private static final Pattern p = Pattern.compile("\\$\\{(.+?)\\}");

    public StringSubstituter(Map<String, String> map) {
        this.map = map;
    }

    public String replace(String str) {
        Matcher m = p.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String var = m.group(1);
            String replacement = map.get(var);
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
