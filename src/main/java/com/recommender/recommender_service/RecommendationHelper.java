package com.recommender.recommender_service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.recommender.dataSource.DataBaseConnection;
import com.recommender.model.Rating;

public class RecommendationHelper {
	
	
	public List<Integer> getPreference(Long uid) throws ClassNotFoundException, SQLException{
		List<Integer> tagsIdList = new LinkedList<Integer>();
		Connection conn = DataBaseConnection.INSTANCE.getJDBCConnection();
		Statement stmt=conn.createStatement();  
		ResultSet rs=stmt.executeQuery(
				"select tag_id from "+RecommenderConstant.USER_TAGS+"where uid="+uid);  
		while(rs.next()){
			tagsIdList.add(rs.getInt(1));
		}
		conn.close(); 
		return tagsIdList;
	}
	
	public List<Rating> getMovies(Long uid) throws SQLException, ClassNotFoundException{
		List<Rating> moviesList = new LinkedList<Rating>();
		Connection conn = DataBaseConnection.INSTANCE.getJDBCConnection();
		Statement stmt=conn.createStatement();  
		ResultSet rs=stmt.executeQuery(
				"SELECT "+ RecommenderConstant.RATING+".uid, "+ RecommenderConstant.RATING+".movie_id,+"+ RecommenderConstant.RATING+".rating"+
				"FROM " + RecommenderConstant.RATING +
				"INNER JOIN" + 
				"(SELECT "+RecommenderConstant.USER_TAGS+".tag_id, "+RecommenderConstant.MOVIE_GENRE+".movie_id"+
				"FROM "+RecommenderConstant.USER_TAGS +
				"INNER JOIN "+ RecommenderConstant.MOVIE_GENRE +
				"ON "+RecommenderConstant.USER_TAGS+".tag_id = "+RecommenderConstant.MOVIE_GENRE+".tag_id" +
				"WHERE "+ RecommenderConstant.RATING+".uid = "+uid+") AS temp"+
				"ON "+RecommenderConstant.RATING+".movie_id = temp.movie_id");  
		while(rs.next()){
			Rating r = new Rating();
			r.setUid(rs.getLong(1));
			r.setMovieId(rs.getLong(2));
			r.setRating(rs.getDouble(3));
			moviesList.add(r);
		}
		return moviesList;
	}
	

	@SuppressWarnings("unchecked")
	public String createMovieFile(Long uid,Object key, String queryName) throws SQLException, ClassNotFoundException, IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(uid+"_movies.txt"), "utf-8"));
		Connection conn = DataBaseConnection.INSTANCE.getJDBCConnection();
		Statement stmt=conn.createStatement(); 
		ResultSet rs = null;
		if(queryName.equals(RecommenderConstant.PREFERENCE)){
			rs=stmt.executeQuery(getbyPreferenceQuery(uid));
		}else if(queryName.equals(RecommenderConstant.YEAR)){
			rs=stmt.executeQuery(getbyYearQuery((List<String>)key));
		}else if(queryName.equals(RecommenderConstant.AGE)){
			rs=stmt.executeQuery(getByAgeGroupdId((Integer)key));
		}
		while(rs.next()){
			Rating r = new Rating();
			r.setUid(rs.getLong(1));
			r.setMovieId(rs.getLong(2));
			r.setRating(rs.getDouble(3));
			writer.write(r.toString());
		}
		writer.close();
		conn.close();
		return uid+"_movies.txt";
	}
	
	public void updatePreferences(List<Integer> tagIdList, long uid) throws SQLException, ClassNotFoundException{
		Connection conn = DataBaseConnection.INSTANCE.getJDBCConnection();
		Statement stmt = conn.createStatement();
		String sql = updatePreferenceQuery(tagIdList, uid);
		stmt.executeUpdate(sql);
		conn.close();
	}
	
	public String getbyPreferenceQuery(long uid){
		return "SELECT "+ RecommenderConstant.RATING+".uid, "+ RecommenderConstant.RATING+".movie_id,"+ RecommenderConstant.RATING+".rating"+
				" FROM " + RecommenderConstant.RATING +
				" INNER JOIN" + 
				" (SELECT "+RecommenderConstant.USER_TAGS+".tag_id, "+RecommenderConstant.MOVIE_GENRE+".movie_id"+
				" FROM "+RecommenderConstant.USER_TAGS +
				" INNER JOIN "+ RecommenderConstant.MOVIE_GENRE +
				" ON "+RecommenderConstant.USER_TAGS+".tag_id = "+RecommenderConstant.MOVIE_GENRE+".tag_id" +
				" WHERE "+ RecommenderConstant.RATING+".uid = "+uid+") AS temp"+
				" ON "+RecommenderConstant.RATING+".movie_id = temp.movie_id";
	}
	
	private String getbyYearQuery(List<String> years){
		String values = "(";
		int count = 0;
		for(String year: years){
			if(years.size()-1==count){
				values = values+ "'" + year + "')";
			}else{
				values = values + "'" + year +"',";
			}
			count++;
		}
		return "SELECT "+ RecommenderConstant.RATING+".uid, "+ RecommenderConstant.RATING+".movie_id,"+ RecommenderConstant.RATING+".rating"+
				" FROM " + RecommenderConstant.RATING +
				" INNER JOIN" + 
				" ON "+RecommenderConstant.MOVIE+".movie_id = "+RecommenderConstant.RATING+".movie_id" +
				" WHERE "+ RecommenderConstant.MOVIE +".year IN "+values;
	}
	
	private String getByAgeGroupdId(Integer ageGroupdId){
		return "SELECT "+ RecommenderConstant.RATING+".uid, "+ RecommenderConstant.RATING+".movie_id,"+ RecommenderConstant.RATING+".rating"+
				" FROM " + RecommenderConstant.RATING +
				" INNER JOIN" + 
				" ON "+RecommenderConstant.RATING+".uid = "+RecommenderConstant.USER_TABLE+".uid" +
				" WHERE "+ RecommenderConstant.USER_TABLE +".ageGroupId ="+ageGroupdId;
	}
	
	public String updatePreferenceQuery(List<Integer> tagIdList, long uid){
		String values = "INSERT INTO "+RecommenderConstant.USER_TAGS+
			    " (uid, tag_id)"+
			    " VALUES ";
		int count = 0;
		for(Integer tagId: tagIdList){
			if(tagIdList.size()-1==count){
				values = values + "("+uid+","+tagId+")";
			}else{
				values = values + "("+uid+","+tagId+"),";
			}
			count++;
		}
		return values;
	}
	
	public String getImdbId(Long uid) throws SQLException, ClassNotFoundException{
		Connection conn = DataBaseConnection.INSTANCE.getJDBCConnection();
		Statement stmt=conn.createStatement();  
		ResultSet rs=stmt.executeQuery(
				"select imdbId from links where movieId="+uid
				);  
		while(rs.next()){
			return rs.getString(1);
		}
		return null;
	}
	
	public IMDBData.MovieRecommendation buildIMDBData(RecommendedItem recommendedItem) throws ClientProtocolException, IOException{
		IMDBData imdbData = new IMDBData();
		IMDBData.MovieRecommendation imdb= imdbData.getImdb("http://www.omdbapi.com/", "i="+recommendedItem.getItemID());
		imdb.setUserRating(String.valueOf(recommendedItem.getValue()));
		return imdb;
	}
	
	public List<RecommendedItem>  getFileBasedRecommendation(String fileName , long uid) throws IOException, TasteException{
		NeighbourBasedRatingRecommendation neighbourBasedRatingRecommendation = new NeighbourBasedRatingRecommendation(fileName);
		List<RecommendedItem> recommendationItems = neighbourBasedRatingRecommendation.getRecommendation(3, uid, 25);
		return recommendationItems;
	}
	
	public List<RecommendedItem> getDataBasedRecommendation(long uid) throws TasteException{
		NeighbourBasedRatingRecommendation neighbourBasedRatingRecommendation = 
				new NeighbourBasedRatingRecommendation(DataBaseConnection.INSTANCE.getDataSource(), "uid", "movie_Id", "rating", RecommenderConstant.RATING);
		List<RecommendedItem> recommendationItems = neighbourBasedRatingRecommendation.getRecommendation(3, uid, 25);
		return recommendationItems;
	}
	
	

}
