package com.recommender.model;

public class Rating {
	
	private Long uid;
	
	private Long movieId;
	
	private Double rating;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return uid + "," + movieId + "," + rating;
	}
	
	

}
