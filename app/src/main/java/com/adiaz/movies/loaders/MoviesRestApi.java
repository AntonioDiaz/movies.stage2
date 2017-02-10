package com.adiaz.movies.loaders;

import com.adiaz.movies.entities.DbMoviesQuery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by toni on 06/02/2017.
 */

public interface MoviesRestApi {

	public static final String BASE_URL = "http://api.themoviedb.org";

	@GET("/3/movie/popular")
	Call<DbMoviesQuery> queryMoviesPopularity(
			@Query("api_key")String apiKey,
			@Query("page") Integer page);

	@GET("/3/movie/top_rated")
	Call<DbMoviesQuery> queryMoviesTopRated(
			@Query("api_key")String apiKey,
			@Query("page") Integer page);
}
