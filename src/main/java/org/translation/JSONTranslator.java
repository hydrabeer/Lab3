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
    private final JSONArray jsonArray;
    private final CountryCodeConverter ccv = new CountryCodeConverter();
    private final String alpha3 = "alpha3";

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

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el.getString(alpha3).equals(countryCode)) {
                for (String k : el.keySet()) {
                    if (!"id".equals(k) && !"alpha2".equals(k) && !alpha3.equals(k)) {
                        if (!k.equals(null)) {
                            out.add(k);
                        }
                    }
                }
            }
        }
        return out;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> out = new ArrayList<>();

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (!el.getString(alpha3).equals(null)) {
                out.add(el.getString(alpha3));
            }
        }

        return out;
    }

    @Override
    public String translate(String country, String languageCode) {
        String countryCode = country;
        // Country may be a name or code. Try assuming it is a code
        if (!this.getCountries().contains(country)) {
            // Convert name to code
            countryCode = ccv.fromCountry(country);
        }

        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject el = this.jsonArray.getJSONObject(i);
            if (el.getString(alpha3).toLowerCase().equals(countryCode.toLowerCase())) {
                return el.getString(languageCode);
            }
        }

        return null;
    }
}
