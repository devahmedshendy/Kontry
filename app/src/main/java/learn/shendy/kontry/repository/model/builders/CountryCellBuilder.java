package learn.shendy.kontry.repository.model.builders;

import learn.shendy.kontry.repository.model.country.CountryCell;
import learn.shendy.kontry.repository.remote.responses.CountryCellResponse;

public class CountryCellBuilder {

    public static CountryCell fromCountryCellResponse(CountryCellResponse response) {
        return new CountryCellBuilder()
                .setName(response.getName())
                .setFlagURL(response.getFlag())
                .setCapital(response.getCapital())
                .setPopulation(response.getPopulation())
                .build();
    }

    private String mName;
    private String mFlagURL;
    private String mCapital;
    private long mPopulation;

    public CountryCell build() {
        CountryCell c = new CountryCell();

        c.setName(mName);
        c.setFlagURL(mFlagURL);
        c.setCapital(mCapital);
        c.setPopulation(mPopulation);

        return c;
    }

    public CountryCellBuilder setName(String name) {
        mName = name;
        return this;
    }

    public CountryCellBuilder setFlagURL(String flagURL) {
        mFlagURL = flagURL;
        return this;
    }

    public CountryCellBuilder setCapital(String capital) {
        mCapital = capital;
        return this;
    }

    public CountryCellBuilder setPopulation(long population) {
        mPopulation = population;
        return this;
    }
}
