package learn.shendy.kontry.repository.model.builders;

import java.util.ArrayList;
import java.util.List;

import learn.shendy.kontry.repository.model.country.Country;
import learn.shendy.kontry.repository.model.country.Currency;
import learn.shendy.kontry.repository.remote.responses.CountryResponse;
import learn.shendy.kontry.repository.remote.responses.LanguageResponse;

public class CountryBuilder {
    private static final String TAG = "CountryBuilder";

    private String mName;
    private String mFlagURL;
    private String mCapital;
    private String mRegion;
    private long mPopulation;
    private Currency mCurrency;
    private List<String> mLanguages = new ArrayList<>();

    public static Country fromCountryResponse(CountryResponse response) {
        CountryBuilder c = new CountryBuilder();

        for (LanguageResponse language : response.getLanguages()) {
            c.addLanguage(language.getNativeName());
        }

        return c
                .setName(response.getName())
                .setFlagURL(response.getFlag())
                .setRegion(response.getRegion())
                .setCapital(response.getCapital())
                .setPopulation(response.getPopulation())
                .setCurrency(CurrencyBuilder.fromCurrencyResponse(response.getCurrencies().get(0)))
                .build();
    }

    public Country build() {
        Country c = new Country();

        c.setName(mName);
        c.setFlagURL(mFlagURL);
        c.setCapital(mCapital);
        c.setRegion(mRegion);
        c.setPopulation(mPopulation);
        c.setCurrency(mCurrency);
        c.setLanguages(mLanguages);

        return c;
    }

    public CountryBuilder setName(String name) {
        this.mName = name;
        return this;
    }

    public CountryBuilder setFlagURL(String flagURL) {
        this.mFlagURL = flagURL;
        return this;
    }

    public CountryBuilder setCapital(String capital) {
        this.mCapital = capital;
        return this;
    }

    public CountryBuilder setRegion(String region) {
        this.mRegion = region;
        return this;
    }

    public CountryBuilder setPopulation(long population) {
        this.mPopulation = population;
        return this;
    }

    public CountryBuilder setCurrency(Currency currency) {
        this.mCurrency = currency;
        return this;
    }

    public CountryBuilder addLanguage(String language) {
        this.mLanguages.add(language);
        return this;
    }

    public CountryBuilder setLanguages(List<String> languages) {
        this.mLanguages = languages;
        return this;
    }
}
