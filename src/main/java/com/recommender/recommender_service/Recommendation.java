package com.recommender.recommender_service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class Recommendation {
	
	
	public List<IMDBData.MovieRecommendation> getMovieRecommendationByPreference(long uid, List<Integer> tagIds) throws ClassNotFoundException, SQLException, IOException, TasteException{
		RecommendationHelper recommendationHelper = new RecommendationHelper();
		if(tagIds!=null && tagIds.size()>0){
			recommendationHelper.updatePreferences(tagIds, uid);;
		}
		List<IMDBData.MovieRecommendation> movies = new LinkedList<>();
		String fileName = recommendationHelper.createMovieFile(uid, null, RecommenderConstant.PREFERENCE);
		List<RecommendedItem> recommendationItems = recommendationHelper.getFileBasedRecommendation(fileName, uid);
		for(RecommendedItem recommendedItem:recommendationItems){
			movies.add(recommendationHelper.buildIMDBData(recommendedItem));
		}
		return movies;
	}
	
	
	public List<IMDBData.MovieRecommendation> getMovieRecommendation(long uid) throws ClassNotFoundException, SQLException, IOException, TasteException{
		List<IMDBData.MovieRecommendation> movies = new LinkedList<>();
		RecommendationHelper recommendationHelper = new RecommendationHelper();
		List<RecommendedItem> recommendationItems = recommendationHelper.getDataBasedRecommendation(uid);
		for(RecommendedItem recommendedItem:recommendationItems){
			movies.add(recommendationHelper.buildIMDBData(recommendedItem));
		}
		return movies;
	}
	
	public List<IMDBData.MovieRecommendation> getMovieRecommendationByYear(long uid, List<String> years) throws ClassNotFoundException, SQLException, IOException, TasteException{
		List<IMDBData.MovieRecommendation> movies = new LinkedList<>();
		RecommendationHelper recommendationHelper = new RecommendationHelper();
		String fileName = recommendationHelper.createMovieFile(uid, years, RecommenderConstant.YEAR);
		List<RecommendedItem> recommendationItems = recommendationHelper.getFileBasedRecommendation(fileName, uid);
		for(RecommendedItem recommendedItem:recommendationItems){
			movies.add(recommendationHelper.buildIMDBData(recommendedItem));
		}
		return movies;
	}
	
	public List<IMDBData.MovieRecommendation> getMovieRecommendationByAge(long uid, Integer ageGroupId) throws ClassNotFoundException, SQLException, IOException, TasteException{
		List<IMDBData.MovieRecommendation> movies = new LinkedList<>();
		RecommendationHelper recommendationHelper = new RecommendationHelper();
		String fileName = recommendationHelper.createMovieFile(uid, ageGroupId, RecommenderConstant.AGE);
		List<RecommendedItem> recommendationItems = recommendationHelper.getFileBasedRecommendation(fileName, uid);
		for(RecommendedItem recommendedItem:recommendationItems){
			movies.add(recommendationHelper.buildIMDBData(recommendedItem));
		}
		return movies;
	}
	

}
