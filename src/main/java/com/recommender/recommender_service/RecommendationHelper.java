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
	

	public String createMovieFile(Long uid) throws SQLException, ClassNotFoundException, IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(uid+"_movies.txt"), "utf-8"));
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
			writer.write(r.toString());
		}
		writer.close();
		return uid+"_movies.txt";
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
	
	
	

}
