package learn.shendy.kontry.utils;

import android.content.Context;
import android.widget.ImageView;

import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;

import learn.shendy.kontry.R;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SVGUtil {
    private static final String TAG = "SVGUtil";

    private static OkHttpClient sHttpClient;

    public static void fetchInto(Context context, String url, final ImageView target) {
        if (sHttpClient == null) {
            sHttpClient = new OkHttpClient
                    .Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1024))
                    .build();
        }

        final Request request = new Request
                .Builder()
                .url(url)
                .build();

        sHttpClient
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        target.setImageResource(R.drawable.placeholder_failure_country_flag);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            InputStream stream = response.body().byteStream();

                            Sharp.loadInputStream(stream).into(target);

                            stream.close();
                        } else {
                            target.setImageResource(R.drawable.placeholder_failure_country_flag);
                        }
                    }
                });
    }

}
