package com.emin.digit.mobile.android.database;

/**
 * Created by Samson on 16/7/4.
 */
public interface EMDatabaseOperator {

    // database
    public void createDatabase();

    public void deleteDatabase();

    public void openDatabase();

    // Table
    public void crateTable();

    public void dropTable();

    public void updateTable();

    // Record
    public void insertRecords();

    public void updateRecords();

    public void deleteRecords();

    public void queryRecords();
}
