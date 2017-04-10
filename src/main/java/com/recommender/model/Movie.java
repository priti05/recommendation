package com.recommender.model;

public class Movie {
	
	private Long movieId;
	
	private String title;
	
	private String year;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return movieId + "," + title + "," + year;
	}
	
	

}
