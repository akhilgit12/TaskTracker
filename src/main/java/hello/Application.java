package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.core.env.Environment;

import hello.DBUtil;

import org.postgresql.ds.PGPoolingDataSource;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

@SpringBootApplication

public class Application extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
		System.setProperty("server.port","8060");
		SpringApplication.run(Application.class, args);
		Connection conn = null;
		try
		{
			conn = DBUtil.getInstance().getDBConn();
			if(conn != null)
			{
				System.out.println("################### DB Configuration here ######################");
		    	String sqlCreate = "CREATE TABLE IF NOT EXISTS TODOLIST (ID SERIAL PRIMARY KEY,TASK_NAME TEXT NOT NULL,DATE BIGINT NOT NULL,STRIKED boolean);";
		    	Statement stmt = conn.createStatement();
		    	stmt.executeUpdate(sqlCreate);
		    }
			else{
				System.out.println("no Exception & no Connection");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				try { conn.close(); } catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    }
}
