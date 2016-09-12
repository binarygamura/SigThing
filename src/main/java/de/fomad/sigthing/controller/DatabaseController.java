package de.fomad.sigthing.controller;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import de.fomad.sigthing.model.Character;

/**
 *
 * @author binary gamura
 */
public class DatabaseController {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class);

    private ComboPooledDataSource databasePool;

    public void initDatabase() throws ClassNotFoundException, SQLException, PropertyVetoException {

	File dbFile = new File("~/sigthing.db");

	databasePool = new ComboPooledDataSource();
	databasePool.setDriverClass("org.h2.Driver");
	databasePool.setJdbcUrl("jdbc:h2:~/sigthing.db");
	databasePool.setUser("");
	databasePool.setPassword("");

	boolean doesFileExist = dbFile.exists();
	if (!doesFileExist) {
	    createStructure();
	}
	LOGGER.info("created database controller.");
    }

    private void createStructure() throws SQLException {
	String[] initQueries = new String[]{
	    "CREATE TABLE IF NOT EXISTS solar_systems (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, name VARCHAR(255) UNIQUE, added_by VARCHAR(255), comment TEXT)",
	    "CREATE TABLE IF NOT EXISTS signatures (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, signature VARCHAR(255), scan_group VARCHAR(255), signal_strength FLOAT, solar_system_id INT, name VARCHAR(255), added_by VARCHAR(255), comment TEXT )",
	    "CREATE TABLE IF NOT EXISTS pilots (id INT PRIMARY KEY NOT NULL, character_name VARCHAR(255) UNIQUE) "};
	try (Connection connection = databasePool.getConnection(); Statement statement = connection.createStatement()) {
	    for (String query : initQueries) {
		statement.executeUpdate(query);
	    }
	}
	LOGGER.info("created database structure.");
    }

    private Signature convertSignature(ResultSet result) throws SQLException{
        Signature signature = new Signature();
        signature.setId(result.getInt("id"));
        signature.setName(result.getString("name"));
        signature.setSignature(result.getString("signature"));
        signature.setSignalStrength(result.getFloat("signal_strength"));
        signature.setSolarSystemId(result.getInt("solar_system_id"));
        signature.setAddedBy(result.getString("added_by"));
        signature.setScanGroup(result.getString("scan_group"));
        signature.setComment(result.getString("comment"));
        return signature;
    }
    
    public List<Signature> querySignaturesFor(int solarSystemId) throws SQLException{
        try(Connection connection = databasePool.getConnection()){
            return querySignaturesFor(connection, solarSystemId);
        }
    }
    
    public void updateComment(Signature signature) throws SQLException{
        String query = "UPDATE signatures SET comment = ? WHERE id = ?";
        try(Connection connection = databasePool.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, signature.getComment());
            statement.setInt(2, signature.getId());
            statement.executeUpdate();
        }
    }
    
    public List<Signature> querySignaturesFor(Connection connection, int solarSystemId) throws SQLException{
        String query = "SELECT comment, id, name, signature, scan_group, signal_strength, solar_system_id, added_by FROM signatures WHERE solar_system_id = ?";
        List<Signature> signatures = new ArrayList<>();
        try(PreparedStatement selectStatement = connection.prepareStatement(query)){
            selectStatement.setInt(1, solarSystemId);
            try(ResultSet result = selectStatement.executeQuery()){
                while(result.next()){
                    signatures.add(convertSignature(result));
                }
            }
        }
        return signatures;
    }

    public void deleteSignature(Signature signature) throws SQLException{
        try(Connection connection = databasePool.getConnection()){
            deleteSignature(connection, signature);
        }
    }
    
    private void deleteSignature(Connection connection, Signature signature) throws SQLException{
        String query = "DELETE FROM signatures WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, signature.getId());
            statement.executeUpdate();
        }
    }
    
    private void insertSignature(Connection connection, Signature signature) throws SQLException {
        String query = "INSERT INTO signatures (signature, signal_strength, scan_group, added_by, solar_system_id, name) VALUES(?,?,?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, signature.getSignature());
            statement.setFloat(2, signature.getSignalStrength());
            statement.setString(3, signature.getScanGroup());
            statement.setString(4, signature.getAddedBy());
            statement.setInt(5, signature.getSolarSystemId());
            statement.setString(6, signature.getName());
            statement.executeUpdate();
        }
    }
    
    private void updateSignature(Connection connection, Signature signature) throws SQLException{
        String query = "UPDATE signatures SET signal_strength = ?, name = ?, scan_group = ? WHERE signature = ? AND solar_system_id = ? AND signal_strength < ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            
            String name = signature.getName().isEmpty() ? signature.getScanGroup() : signature.getName();
            
            statement.setFloat(1, signature.getSignalStrength());
            statement.setString(2, name);
            statement.setString(3, signature.getScanGroup());
            statement.setString(4, signature.getSignature());
            statement.setInt(5, signature.getSolarSystemId());
            statement.setFloat(6, signature.getSignalStrength());
            statement.executeUpdate();
        }
    }
    
    public void mergeSignatures(List<Signature> signatures, Character character, int solarSystemId) throws SQLException{
        try(Connection connection = databasePool.getConnection()){
//            connection.setAutoCommit(false);
            
            List<Signature> currentSignatures = querySignaturesFor(connection, solarSystemId);

            List<Signature> deleteList = new ArrayList<>(currentSignatures);
            deleteList.removeAll(signatures);

            //remove all signatures which are not part of the update.
            for(Signature toDelete : deleteList){
                deleteSignature(connection, toDelete);
            }
            
            for(Signature signature : signatures){
                if(currentSignatures.contains(signature)){
                    updateSignature(connection, signature);
                }
                else {
                    signature.setAddedBy(character.getName());
                    signature.setSolarSystemId(solarSystemId);
                    insertSignature(connection, signature);
                }
            }
//            connection.commit();
//            connection.setAutoCommit(true);
        }
    }
    
    public void save(SolarSystem solarSystem, Character character) throws SQLException {
	String query = "MERGE INTO solar_systems (id, name, added_by) VALUES (?,?,?)";
	try (Connection connection = databasePool.getConnection(); PreparedStatement insertStatement = connection.prepareStatement(query)) {
	    insertStatement.setInt(1, solarSystem.getId());
	    insertStatement.setString(2, solarSystem.getName());
            insertStatement.setString(3, character.getName());
	    insertStatement.executeUpdate();
	}
    }
}
