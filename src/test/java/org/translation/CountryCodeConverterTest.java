package org.translation;

import org.junit.Test;

import static org.junit.Assert.*;

public class CountryCodeConverterTest {

    @Test
    public void fromCountryCodeUSA() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals("United States of America (the)", converter.fromCountryCode("usa"));
    }

    @Test
    public void fromCountryCodeCanada() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals("Canada", converter.fromCountryCode("can"));
    }

    @Test
    public void fromCountryCodeChina() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals("China", converter.fromCountryCode("chn"));
    }

    @Test
    public void fromCountryNameCanada() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals("can", converter.fromCountry("Canada"));
    }

    @Test
    public void fromCountryNameChina() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals("chn", converter.fromCountry("China"));
    }

    @Test
    public void fromCountryCodeAllLoaded() {
        CountryCodeConverter converter = new CountryCodeConverter();
        assertEquals(249, converter.getNumCountries());
    }
}
