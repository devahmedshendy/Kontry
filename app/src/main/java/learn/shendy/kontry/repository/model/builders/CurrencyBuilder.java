package learn.shendy.kontry.repository.model.builders;

import learn.shendy.kontry.repository.model.country.Currency;
import learn.shendy.kontry.repository.remote.responses.CurrencyResponse;

public class CurrencyBuilder {
    private String mCode;
    private String mName;
    private String mSymbol;

    public static Currency fromCurrencyResponse(CurrencyResponse currencyResponse) {
        return new CurrencyBuilder()
                .setCode(currencyResponse.getCode())
                .setName(currencyResponse.getName())
                .setSymbol(currencyResponse.getSymbol())
                .build();
    }

    public Currency build() {
        Currency c = new Currency();

        c.setName(mName);
        c.setCode(mCode);
        c.setSymbol(mSymbol);

        return c;
    }

    public CurrencyBuilder setCode(String code) {
        mCode = code;
        return this;
    }

    public CurrencyBuilder setName(String name) {
        mName = name;
        return this;
    }

    public CurrencyBuilder setSymbol(String symbol) {
        mSymbol = symbol;
        return this;
    }
}
