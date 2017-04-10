package com.recommender.recommender_service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.mysql.cj.jdbc.MysqlDataSource;

public class NeighbourBasedRatingRecommendation {
	
	private DataModel model = null;
	
	public NeighbourBasedRatingRecommendation(MysqlDataSource dataSource, String userIdColumn, 
			String movieIdColumn, String ratingColumn, String tableName){
		model = new MySQLJDBCDataModel(dataSource,
				tableName, userIdColumn, movieIdColumn, ratingColumn, null);
	}
	
	public NeighbourBasedRatingRecommendation(String fileName) throws IOException{
		 model = new FileDataModel(new File(fileName));
	}
	
	public List<RecommendedItem> getRecommendation(double threshold, long userId, int numberOfRecommendation) throws TasteException{
		//Creating UserSimilarity object.
        UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(model);
        //Creating UserNeighbourHHood object.
        UserNeighborhood userneighborhood = new ThresholdUserNeighborhood(threshold, usersimilarity, model);
        //Create UserRecomender
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, userneighborhood, usersimilarity);
        List<RecommendedItem> recommendations = recommender.recommend(userId, numberOfRecommendation);
        return recommendations;
	}

}
