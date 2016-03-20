package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {

    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) throws Exception {
        return new ArrayList<String>() {
            {
                for (int i = 0; i < numberOfResults; i++) {
                    RegexParser parser = new RegexParser(regEx, maxLength);
                    String result = parser.getRandomString();
                    add(result);
                }
            }
        };
    }


}