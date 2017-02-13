package com.adiaz.movies.loaders;

import com.adiaz.movies.entities.DbMoviesQuery;
import com.adiaz.movies.entities.reviews.DbMoviesReviews;
import com.adiaz.movies.entities.videos.DbMoviesVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* Created by toni on 06/02/2017. */

public interface MoviesRestApi {

	@GET("/3/movie/popular")
	Call<DbMoviesQuery> queryMoviesPopularity(
			@Query("api_key")String apiKey,
			@Query("page") Integer page);

	@GET("/3/movie/top_rated")
	Call<DbMoviesQuery> queryMoviesTopRated(
			@Query("api_key")String apiKey,
			@Query("page") Integer page);

	@GET("/3/movie/{idMovie}/videos")
	Call<DbMoviesVideos> queryMoviesVideos(
			@Path("idMovie") int idMovie,
			@Query("api_key") String apiKey);

	@GET("/3/movie/{idMovie}/reviews")
	Call<DbMoviesReviews> queryMoviesReviews(
			@Path("idMovie") int idMovie,
			@Query("api_key") String apiKey);

}
