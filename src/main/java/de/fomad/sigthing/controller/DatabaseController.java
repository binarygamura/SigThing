package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.Signature;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class DatabaseController 
{
    private Logger LOGGER = LogManager.getLogger(DatabaseController.class);
    
    private Connection connection;
    
    public void initDatabase() throws ClassNotFoundException, SQLException{
	
	Class.forName("org.h2.Driver"); 
	File dbFile = new File("~/sigthing.db");
	boolean doesFileExist = dbFile.exists();
	connection = DriverManager.getConnection( "jdbc:h2:~/sigthing.db", "", ""); 
	if(!doesFileExist){
	    createStructure();
	}
	LOGGER.info("created database controller.");
    }
    
    private void createStructure() throws SQLException{
	String createSolarSystemQuery = "CREATE TABLE IF NOT EXISTS solar_systems (ID INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, NAME VARCHAR(255) UNIQUE)";
	String createSignaturesQuery = "CREATE TABLE IF NOT EXISTS signatures (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, name VARCHAR(255), scan_group VARCHAR(255), signal_strength FLOAT, solar_system_id INT )";
	String createUserTable = "CREATE TABLE IF NOT EXISTS pilots (character_id INT PRIMARY KEY NOT NULL, character_name VARCHAR(255) UNIQUE ";
	try(Statement statement = connection.createStatement()){
	    statement.executeUpdate(createSolarSystemQuery);
	    statement.executeUpdate(createSignaturesQuery);
	}
	LOGGER.info("created database structure.");
    }
    
    public List<Signature> getSignatures(String solarSystemId){
	return null;
    }
}
