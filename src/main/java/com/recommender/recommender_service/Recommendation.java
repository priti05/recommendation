package com.recommender.recommender_service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class Recommendation {
	
	
	public List<IMDBData.MovieRecommendation> getMovieRecommendationByPreference(long uid) throws ClassNotFoundException, SQLException, IOException, TasteException{
		List<IMDBData.MovieRecommendation> movies = new LinkedList<>();
		RecommendationHelper recommendationHelper = new RecommendationHelper();
		String fileName = recommendationHelper.createMovieFile(uid);
		NeighbourBasedRatingRecommendation neighbourBasedRatingRecommendation = new NeighbourBasedRatingRecommendation(fileName);
		List<RecommendedItem> recommendationItems = neighbourBasedRatingRecommendation.getRecommendation(3, uid, 25);
		for(RecommendedItem recommendedItem:recommendationItems){
			IMDBData imdbData = new IMDBData();
			IMDBData.MovieRecommendation imdb= imdbData.getImdb("http://www.omdbapi.com/", "i="+recommendedItem.getItemID());
			imdb.setUserRating(String.valueOf(recommendedItem.getValue()));
			movies.add(imdb);
		}
		return movies;
	}
	

}
