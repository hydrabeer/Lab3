package org.translation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LanguageCodeConverterTest {

    @Test
    public void fromLanguageCodeEN() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("English", converter.fromLanguageCode("en"));
    }

    @Test
    public void fromLanguageCodeFR() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("French", converter.fromLanguageCode("fr"));
    }

    @Test
    public void fromLanguageCodeTR() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("Turkish", converter.fromLanguageCode("tr"));
    }

    @Test
    public void fromLanguageNameEN() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("en", converter.fromLanguage("English"));
    }

    @Test
    public void fromLanguageNameFR() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("fr", converter.fromLanguage("French"));
    }

    @Test
    public void fromLanguageNameTR() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals("tr", converter.fromLanguage("Turkish"));
    }

    @Test
    public void fromLanguageCodeAllLoaded() {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        assertEquals(184, converter.getNumLanguages());
    }
}
