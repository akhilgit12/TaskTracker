package hello;

import org.postgresql.ds.PGPoolingDataSource;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class DBUtil{
	public PGPoolingDataSource source;
	private static DBUtil dbUtil = null;

	public synchronized static DBUtil getInstance() {
		if (dbUtil == null) {
			System.out.println(":::::::in DButil getInstance creating an instance::::::::");
			dbUtil = new DBUtil();
			//createSource();
		}
		return dbUtil;
	}
	private DBUtil(){
		source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("springbootdb");
		source.setUser("postgres");
		source.setPassword("akhildb");
		source.setMaxConnections(10);
	}
	public Connection getDBConn(){
		Connection conn = null;
		try{
			 conn = source.getConnection();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
}