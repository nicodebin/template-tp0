package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) throws Exception {
        RegExGenerator generator = new RegExGenerator(5);

        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() {
        try {
            assertTrue(validate(".", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testMultipleCharacters() {
        try {
            assertTrue(validate("...", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testLiteral() {
        try {
            assertTrue(validate("\\@", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testLiteralDotCharacter() {
        try {
            assertTrue(validate("\\@..", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testZeroOrOneCharacter() {
        try {
            assertTrue(validate("\\@.h?", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testCharacterSet() {
        try {
            assertTrue(validate("[abc]", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        try {
            assertTrue(validate("[abc]+", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClosingLiteralSetNotationInsideSet() {
        try{
            assertTrue(validate("[\\]]+", 1));
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testInvalidRegexEmptySet() {
        try{
            validate("[]", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexEmptySetWithCuantifier() {
        try{
            validate("[]+", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexUnclosedSet() {
        try{
            validate("[pepe", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexUnclosedSet2() {
        try{
            validate("pe]+pe", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexQuantifierAsterisco() {
        try{
            validate("*", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexQuantifierPregunta() {
        try{
            validate("?", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

    @Test
    public void testInvalidRegexQuantifierSuma() {
        try{
            validate("+", 1);
            fail("InvalidRegexException expected.");
        }catch(Exception e){
            assertTrue(e instanceof InvalidRegexException);
        }
    }

}
