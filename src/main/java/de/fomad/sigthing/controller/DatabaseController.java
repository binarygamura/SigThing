package de.fomad.sigthing.controller;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
	    "CREATE TABLE IF NOT EXISTS solar_systems (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, name VARCHAR(255) UNIQUE)",
	    "CREATE TABLE IF NOT EXISTS signatures (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, name VARCHAR(255), scan_group VARCHAR(255), signal_strength FLOAT, solar_system_id INT )",
	    "CREATE TABLE IF NOT EXISTS pilots (id INT PRIMARY KEY NOT NULL, character_name VARCHAR(255) UNIQUE) "};
	try (Connection connection = databasePool.getConnection(); Statement statement = connection.createStatement()) {
	    for (String query : initQueries) {
		statement.executeUpdate(query);
	    }
	}
	LOGGER.info("created database structure.");
    }

    public List<Signature> getSignatures(String solarSystemId) {
	return null;
    }

    public void save(SolarSystem solarSystem) throws SQLException {
	String query = "MERGE INTO solar_systems (id, name) VALUES (?,?)";
	try (Connection connection = databasePool.getConnection(); PreparedStatement insertStatement = connection.prepareStatement(query)) {
	    insertStatement.setInt(1, solarSystem.getId());
	    insertStatement.setString(2, solarSystem.getName());
	    insertStatement.executeUpdate();
	}
    }
}
