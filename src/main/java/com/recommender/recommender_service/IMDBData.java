package com.recommender.recommender_service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class IMDBData {
	
	public MovieRecommendation getImdb(String url, String body) throws ClientProtocolException, IOException {
         CloseableHttpClient httpClient = HttpClientBuilder.create().build();
         HttpPost request = new HttpPost(url);
         StringEntity params = new StringEntity(body);
         request.addHeader("content-type", "application/json");
         request.setEntity(params);
         HttpResponse result = httpClient.execute(request);
         String json = EntityUtils.toString(result.getEntity(), "UTF-8");
         com.google.gson.Gson gson = new com.google.gson.Gson();
         MovieRecommendation movieRecommendation = gson.fromJson(json, MovieRecommendation.class);
        return movieRecommendation;
    }
	
	class MovieRecommendation{
		private String Title;
		private String Year;
		private String Released;
		private String Runtime;
		private String Genre;
		private String Director;
		private String Actors;
		private String Plot;
		private String Language;
		private String Country;
		private String Awards;
		private String Poster;
		private String imdbRating;
		private String imdbVotes;
		private String userRating;
		
		public String getUserRating() {
			return userRating;
		}
		public void setUserRating(String userRating) {
			this.userRating = userRating;
		}
		public String getTitle() {
			return Title;
		}
		public void setTitle(String title) {
			Title = title;
		}
		public String getYear() {
			return Year;
		}
		public void setYear(String year) {
			Year = year;
		}
		public String getReleased() {
			return Released;
		}
		public void setReleased(String released) {
			Released = released;
		}
		public String getRuntime() {
			return Runtime;
		}
		public void setRuntime(String runtime) {
			Runtime = runtime;
		}
		public String getGenre() {
			return Genre;
		}
		public void setGenre(String genre) {
			Genre = genre;
		}
		public String getDirector() {
			return Director;
		}
		public void setDirector(String director) {
			Director = director;
		}
		public String getActors() {
			return Actors;
		}
		public void setActors(String actors) {
			Actors = actors;
		}
		public String getPlot() {
			return Plot;
		}
		public void setPlot(String plot) {
			Plot = plot;
		}
		public String getLanguage() {
			return Language;
		}
		public void setLanguage(String language) {
			Language = language;
		}
		public String getCountry() {
			return Country;
		}
		public void setCountry(String country) {
			Country = country;
		}
		public String getAwards() {
			return Awards;
		}
		public void setAwards(String awards) {
			Awards = awards;
		}
		public String getPoster() {
			return Poster;
		}
		public void setPoster(String poster) {
			Poster = poster;
		}
		public String getImdbRating() {
			return imdbRating;
		}
		public void setImdbRating(String imdbRating) {
			this.imdbRating = imdbRating;
		}
		public String getImdbVotes() {
			return imdbVotes;
		}
		public void setImdbVotes(String imdbVotes) {
			this.imdbVotes = imdbVotes;
		}
		
		
	}

}
