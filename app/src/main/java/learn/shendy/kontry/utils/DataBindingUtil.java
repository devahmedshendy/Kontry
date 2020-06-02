package learn.shendy.kontry.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.List;

import learn.shendy.kontry.repository.model.country.Currency;

public class DataBindingUtil {
    @BindingAdapter("setCurrency")
    public static void setCurrency(TextView textView, Currency currency) {
        String c = String.format("%s - %s", currency.getName(), currency.getSymbol());

        textView.setText(c);
    }

    @BindingAdapter("setLanguages")
    public static void setLanguages(TextView textView, List<String> languages) {
        textView.setText(languages.toString().substring(1, languages.toString().length() - 1));
    }
}
