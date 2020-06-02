package learn.shendy.kontry.repository.model.country;

import learn.shendy.kontry.repository.model.builders.CountryCellBuilder;

public class CountryCell {

    private String mName;
    private String mFlagURL;
    private String mCapital;
    private long mPopulation;
    private boolean mExpanded = false;

    public static CountryCellBuilder Builder() {
        return new CountryCellBuilder();
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

    public long getPopulation() {
        return mPopulation;
    }

    public void setPopulation(long population) {
        mPopulation = population;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
    }

    public void flipExpanded() {
        mExpanded = !mExpanded;
    }

    @Override
    public String toString() {
        return "CountryCell {" +
                "mName = '" + mName + '\'' +
                ", mFlagURL = '" + mFlagURL + '\'' +
                ", mCapital = '" + mCapital + '\'' +
                ", mPopulation = " + mPopulation +
                " } ";
    }
}
