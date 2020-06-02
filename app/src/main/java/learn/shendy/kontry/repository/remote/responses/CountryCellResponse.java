package learn.shendy.kontry.repository.remote.responses;

public class CountryCellResponse {
    /**
     * name : Afghanistan
     * flag : https://restcountries.eu/data/afg.svg
     * region : Asia
     * population : 27657145
     */

    private String name;
    private String flag;
    private String capital;
    private long population;

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

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "CountryCellResponse { " +
                "name = '" + name + '\'' +
                ", flag = '" + flag + '\'' +
                ", capital = '" + capital + '\'' +
                ", population = " + population +
                " } ";
    }
}
