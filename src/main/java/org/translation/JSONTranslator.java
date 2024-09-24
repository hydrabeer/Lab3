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
    public List<String> getCountryLanguages(String countryCode) {
        ArrayList<String> out = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i ++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el.getString("alpha3").equals(countryCode))
                for (String k : el.keySet()) {
                    if (!k.equals("id") && !k.equals("alpha2") && !k.equals("alpha3")) {
                        if (!k.equals(null))
                            out.add(k);
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
            if (!el.getString("alpha3").equals(null))
                out.add(el.getString("alpha3"));
        }

        return out;
    }

    @Override
    public String translate(String country, String languageCode) {
        if (country.length() != 3) {
            country = (new CountryCodeConverter()).fromCountry(country);
        }
        if (!this.getCountries().contains(country)) {
            return null;
        }

        for (int i = 0; i < this.jsonArray.length(); i ++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el.getString("alpha3").toLowerCase().equals(country.toLowerCase()))
                return el.getString(languageCode);
        }

        return null;
    }
}
