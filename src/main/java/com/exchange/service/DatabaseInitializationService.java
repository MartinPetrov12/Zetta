package com.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

/**
 * DatabaseInitializationService class.
 *
 * Initializes the database schema for the application.
 *
 * @author [Your Name]
 * @version [Date]
 */
@Service
public class DatabaseInitializationService {

    /**
     * Injected JdbcTemplate instance for interacting with the database.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Initializes the database schema upon creation of this bean.
     *
     * This method attempts to create the "Conversion" table.
     * Any errors encountered during table creation are logged to the console but don't prevent application startup.
     */
    @PostConstruct
    public void initializeDatabases() {
        try {
            jdbcTemplate.execute("CREATE TABLE Conversion (\n" +
                    "    transactionId INT,\n" +
                    "    transactionDate TIMESTAMP,\n" +
                    "    base VARCHAR(255),\n" +
                    "    quote VARCHAR(255),\n" +
                    "    rate DOUBLE,\n" +
                    "    amount DOUBLE\n" +
                    ");");
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}