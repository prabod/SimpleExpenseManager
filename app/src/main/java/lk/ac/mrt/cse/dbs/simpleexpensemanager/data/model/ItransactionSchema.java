package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

/**
 * Created by prabod on 11/19/16.
 */

public interface ItransactionSchema {
    String TRANSACTION_TABLE = "transactions";
    String COLUMN_ID = "_id";
    String COLUMN_DATE = "date";
    String COLUMN_ACCOUNT_NO = "account_no";
    String COLUMN_EXPENSE_TYPE = "expense_type";
    String COLUMN_AMOUNT = "amount";
    String TRANSACTION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TRANSACTION_TABLE
            + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY, "
            + COLUMN_ACCOUNT_NO
            + " TEXT NOT NULL, "
            + COLUMN_DATE
            + " DATE, "
            + COLUMN_EXPENSE_TYPE
            + " TEXT, "
            + COLUMN_AMOUNT
            + " DOUBLE, "
            + " FOREIGN KEY (account_no) REFERENCES Account(account_no) )";

    String[] TRANSACTION_COLUMNS = new String[] { COLUMN_ID,
            COLUMN_ACCOUNT_NO, COLUMN_DATE, COLUMN_EXPENSE_TYPE,COLUMN_AMOUNT };
    String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;
}
