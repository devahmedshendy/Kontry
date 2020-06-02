package learn.shendy.kontry.repository.remote;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import learn.shendy.kontry.repository.remote.responses.CountryCellResponse;
import learn.shendy.kontry.repository.remote.responses.CountryResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestCountriesService {

    @GET("all")
    Observable<List<CountryCellResponse>> getAll(
            @Query("fields") String fields
    );

    @GET("name/{fullName}?fullText=true")
    Maybe<List<CountryResponse>> getCountryByFullName(
            @Path("fullName") String fullName,
            @Query("fields") String fields
    );

    @GET("name/{name}")
    Observable<List<CountryCellResponse>> getCountryByName(
            @Path("name") String name,
            @Query("fields") String fields
    );
}
