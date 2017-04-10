package com.recommender.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DataBaseConnection {
	
	 INSTANCE;
	
	
	 public MysqlDataSource getDataSource(){
		 MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName("localhost");
			dataSource.setUser("root");
			dataSource.setPassword("password");
			dataSource.setDatabaseName("mia01");
			return dataSource;			
	 }
	 
	 public Connection getJDBCConnection() throws ClassNotFoundException, SQLException{
		 Class.forName("com.mysql.jdbc.Driver");  
		 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sonoo","root","root");
		 return con;
	 }
	

}
