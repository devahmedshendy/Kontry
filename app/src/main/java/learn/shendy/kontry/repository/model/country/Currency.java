package learn.shendy.kontry.repository.model.country;

import learn.shendy.kontry.repository.model.builders.CurrencyBuilder;

public class Currency {
    private String mCode;
    private String mName;
    private String mSymbol;

    public static CurrencyBuilder Builder() {
        return new CurrencyBuilder();
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    @Override
    public String toString() {
        return "Currency { " +
                "mCode = '" + mCode + '\'' +
                ", mName = '" + mName + '\'' +
                ", mSymbol = '" + mSymbol + '\'' +
                " } ";
    }
}
