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
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private JSONArray jsonArray;
    private CountryCodeConverter ccv = new CountryCodeConverter();
    private LanguageCodeConverter lcv = new LanguageCodeConverter();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            this.jsonArray = new JSONArray(jsonString);
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        String code = ccv.fromCountry(country);
        ArrayList<String> out = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i ++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el == JSONObject.NULL || el == null || el.getString("alpha3") != code)
                continue;

            for (String k : el.keySet()) {
                if (k != "id" && k != "alpha2" && k != "alpha3") {
                    out.add(el.getString(k));
                }
            }
        }
        return out;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> out = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i ++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el == JSONObject.NULL || el == null) continue;
            out.add(el.getString("alpha3"));
        }

        return out;
    }

    @Override
    public String translate(String country, String language) {
        String contCode = ccv.fromCountry(country);
        String langCode = lcv.fromLanguage(language);

        if (!this.getCountries().contains(contCode)) {
            return null;
        }

        for (int i = 0; i < this.jsonArray.length(); i ++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el == JSONObject.NULL || el == null || el.getString("alpha3") != code)
                continue;
            return el.getString(langCode);
        }

        return null;
    }
}
