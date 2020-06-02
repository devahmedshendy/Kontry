package learn.shendy.kontry.repository.model.country;

import java.io.Serializable;
import java.util.List;

import learn.shendy.kontry.repository.model.builders.CountryBuilder;

public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mName;
    private String mFlagURL;
    private String mCapital;
    private String mRegion;
    private long mPopulation;
    private Currency mCurrency;
    private List<String> mLanguages;

    public static CountryBuilder Builder() {
        return new CountryBuilder();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFlagURL() {
        return mFlagURL;
    }

    public void setFlagURL(String flagURL) {
        mFlagURL = flagURL;
    }

    public String getCapital() {
        return mCapital;
    }

    public void setCapital(String capital) {
        mCapital = capital;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public long getPopulation() {
        return mPopulation;
    }

    public void setPopulation(long population) {
        mPopulation = population;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        mCurrency = currency;
    }

    public List<String> getLanguages() {
        return mLanguages;
    }

    public void setLanguages(List<String> languages) {
        mLanguages = languages;
    }

    @Override
    public String toString() {
        return "Country { " +
                "mName = '" + mName + '\'' +
                ", mFlagURL = '" + mFlagURL + '\'' +
                ", mCapital = '" + mCapital + '\'' +
                ", mRegion = '" + mRegion + '\'' +
                ", mPopulation = " + mPopulation +
                ", mCurrency = " + mCurrency +
                ", mLanguages = " + mLanguages +
                " } ";
    }
}
