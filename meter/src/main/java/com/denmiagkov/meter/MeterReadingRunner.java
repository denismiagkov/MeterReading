package com.denmiagkov.meter;

import com.denmiagkov.meter.utils.App;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

public class MeterReadingRunner {
    public static void main(String[] args) throws SQLException, LiquibaseException {
        App.init();
    }
}
