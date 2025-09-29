package com.myl117.authservice.authservice;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.impossibl.postgres.jdbc.PGDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		System.out.println("DB URL: " + System.getenv("DB_URL"));
		System.out.println("DB Username: " + System.getenv("DB_USERNAME"));

		SpringApplication.run(AuthserviceApplication.class, args);
	}

	// // ✅ Test using Spring DataSource (HikariCP)
	// @Bean
	// public CommandLineRunner testSpringConnection(DataSource dataSource) {
	// 	return args -> {
	// 		try (Connection conn = dataSource.getConnection()) {
	// 			System.out.println("✅ Spring DataSource connection successful!");
	// 			System.out.println("Connected to: " + conn.getMetaData().getURL());
	// 		} catch (Exception e) {
	// 			System.err.println("❌ Spring DataSource connection failed!");
	// 			e.printStackTrace();
	// 		}
	// 	};
	// }

	@Bean
public CommandLineRunner testRawJdbcConnection() {
    return args -> {
        PGDataSource ds = new PGDataSource();

        ds.setServerName("<url>");
        ds.setDatabaseName("<db>");
        // ds.setUser("");
        // ds.setPassword("");
				ds.setSslMode("disable");

        // Disable SSL
        //ds.setProperty("ssl", "false");

        try (Connection conn = ds.getConnection()) {
            System.out.println("✅ Raw JDBC connection successful!");
            System.out.println("Connected to: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("❌ Raw JDBC connection failed!");
            e.printStackTrace();
        }
    };
}

	
}
