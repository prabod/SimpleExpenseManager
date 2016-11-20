package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

/**
 * Created by prabod on 11/19/16.
 */

public interface IAccountSchema {
    String ACCOUNT_TABLE = "Account";
    String COLUMN_ID = "_id";
    String COLUMN_ACCOUNT_HOLDER_NAME = "account_holder_name";
    String COLUMN_ACCOUNT_NO = "account_no";
    String COLUMN_BANK_NAME = "bank_name";
    String COLUMN_BALANCE = "balance";
    String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ACCOUNT_TABLE
            + " ("
            + COLUMN_ID
            + " INTEGER , "
            + COLUMN_ACCOUNT_NO
            + " TEXT PRIMARY KEY, "
            + COLUMN_ACCOUNT_HOLDER_NAME
            + " TEXT, "
            + COLUMN_BANK_NAME
            + " TEXT, "
            + COLUMN_BALANCE
            + " FLOAT "
            + ")";

    String[] ACCOUNT_COLUMNS = new String[] { COLUMN_ID,
            COLUMN_ACCOUNT_NO, COLUMN_ACCOUNT_HOLDER_NAME, COLUMN_BANK_NAME , COLUMN_BALANCE};
    String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;
}
