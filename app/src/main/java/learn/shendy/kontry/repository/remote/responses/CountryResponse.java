package learn.shendy.kontry.repository.remote.responses;

import java.util.List;

public class CountryResponse {
    /**
     * currencies : [{"code":"AFN","name":"Afghan afghani","symbol":"؋"}]
     * languages : [{"iso639_1":"ps","iso639_2":"pus","name":"Pashto","nativeName":"پښتو"},{"iso639_1":"uz","iso639_2":"uzb","name":"Uzbek","nativeName":"Oʻzbek"},{"iso639_1":"tk","iso639_2":"tuk","name":"Turkmen","nativeName":"Türkmen"}]
     * name : Afghanistan
     * capital : Kabul
     * region : Asia
     * population : 27657145
     */

    private String name;
    private String flag;
    private String capital;
    private String region;
    private long population;
    private List<CurrencyResponse> currencies;
    private List<LanguageResponse> languages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public List<CurrencyResponse> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyResponse> currencies) {
        this.currencies = currencies;
    }

    public List<LanguageResponse> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageResponse> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "CountryResponse { " +
                "name = '" + name + '\'' +
                ", flag = '" + flag + '\'' +
                ", capital = '" + capital + '\'' +
                ", region = '" + region + '\'' +
                ", population = " + population +
                ", currencies = " + currencies +
                ", languages = " + languages +
                " } ";
    }
}
