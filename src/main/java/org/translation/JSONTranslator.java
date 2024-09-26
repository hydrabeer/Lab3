package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads the translation
 * data from a JSON file. The data is read once each time an instance of this
 * class is constructed.
 */
public class JSONTranslator implements Translator {

    private static final String ALPHA3 = "alpha3";
    private static final String ALPHA2 = "alpha2";
    private static final String ID = "id";

    private JSONArray jsonArray;
    private final CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources
     * file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified
     * resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file cannot be loaded properly
     */
    public JSONTranslator(String filename) {
        try {
            String jsonString = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            this.jsonArray = new JSONArray(jsonString);
        } 
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves a list of language codes available for a given country code.
     *
     * @param countryCode the three-letter country code
     * @return a list of language codes
     */
    @Override
    public List<String> getCountryLanguages(String countryCode) {
        List<String> languageCodes = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject countryObject = this.jsonArray.getJSONObject(i);
            if (countryObject.getString(ALPHA3).equals(countryCode)) {
                for (String key : countryObject.keySet()) {
                    if (!ID.equals(key) && !ALPHA2.equals(key) && !ALPHA3.equals(key)) {
                        languageCodes.add(key);
                    }
                }
            }
        }
        return languageCodes;
    }

    /**
     * Retrieves a list of country codes available in the JSON data.
     *
     * @return a list of country codes
     */
    @Override
    public List<String> getCountries() {
        List<String> countryCodes = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject countryObject = this.jsonArray.getJSONObject(i);
            String alpha3 = countryObject.optString(ALPHA3, null);
            if (alpha3 != null) {
                countryCodes.add(alpha3);
            }
        }

        return countryCodes;
    }

    /**
     * Translates a country's name or code into the specified language.
     *
     * @param country the country name or code
     * @param languageCode the target language code
     * @return the translated country name, or null if not found
     */
    @Override
    public String translate(String country, String languageCode) {
        String result = null;
        String countryCode = country;
        if (!this.getCountries().contains(countryCode)) {
            countryCode = countryCodeConverter.fromCountry(countryCode);
        }
        if (this.getCountries().contains(countryCode)) {
            for (int i = 0; i < this.jsonArray.length(); i++) {
                JSONObject countryObject = this.jsonArray.getJSONObject(i);
                if (countryObject.getString(ALPHA3).equalsIgnoreCase(countryCode)) {
                    result = countryObject.optString(languageCode, null);
                    break;
                }
            }
        }
        return result;
    }
}
