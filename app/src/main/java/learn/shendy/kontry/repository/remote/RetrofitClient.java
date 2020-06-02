package learn.shendy.kontry.repository.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit REST_COUNTRIES_CLIENT;

    public static RestCountriesService getRestCountriesService() {
        final String baseURL = "https://restcountries.eu/rest/v2/";
        return singletonClient(baseURL).create(RestCountriesService.class);
    }

    private static Retrofit singletonClient(String baseURL) {
        if (REST_COUNTRIES_CLIENT == null) {
            REST_COUNTRIES_CLIENT = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return REST_COUNTRIES_CLIENT;
    }

//    public static Retrofit singletonInstance(String fields) {
//        if (INSTANCE == null) {
//            INSTANCE = new Retrofit.Builder()
//                    .baseUrl(mBaseURL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .client(injectFields(fields))
//                    .build();
//        }
//
//        return INSTANCE;
//    }

//    private static OkHttpClient injectFields(String fields) {
//        return new OkHttpClient.Builder()
//                .addInterceptor(chain -> {
//                    Request originalReq = chain.request();
//                    HttpUrl originalUrl = originalReq.url();
//
//                    HttpUrl updatedUrl = originalUrl.newBuilder()
//                            .addQueryParameter("fields", fields)
//                            .build();
//
//                    Request updatedReq = originalReq.newBuilder()
//                            .url(updatedUrl)
//                            .build();
//
//                    return chain.proceed(updatedReq);
//                })
//                .build();
//    }
}
