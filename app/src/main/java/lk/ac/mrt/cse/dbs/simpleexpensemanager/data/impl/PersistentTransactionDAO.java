package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseManagerHelperDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ItransactionSchema;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by prabod on 11/20/16.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private ExpenseManagerHelperDB emDBHelper;
    private SQLiteDatabase db;

    public PersistentTransactionDAO(Context context) {
        emDBHelper = new ExpenseManagerHelperDB(context);
        db = emDBHelper.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues values = new ContentValues();
        values.put(ItransactionSchema.COLUMN_ACCOUNT_NO, accountNo);
        values.put(ItransactionSchema.COLUMN_DATE, date.toString());
        values.put(ItransactionSchema.COLUMN_EXPENSE_TYPE, expenseType.toString());
        values.put(ItransactionSchema.COLUMN_AMOUNT, amount);
        long newRowId = db.insert(ItransactionSchema.TRANSACTION_TABLE, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String sortOrder =
                ItransactionSchema.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                ItransactionSchema.TRANSACTION_TABLE,                     // The table to query
                ItransactionSchema.TRANSACTION_COLUMNS,                   // The columns to return
                null,                                             // The columns for the WHERE clause
                null,                                             // The values for the WHERE clause
                null,                                             // don't group the rows
                null,                                             // don't filter by row groups
                sortOrder                                         // The sort order
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Transaction transaction = cursorToEntity(cursor);
                transactionList.add(transaction);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String sortOrder =
                ItransactionSchema.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                ItransactionSchema.TRANSACTION_TABLE,                     // The table to query
                ItransactionSchema.TRANSACTION_COLUMNS,                   // The columns to return
                null,                                             // The columns for the WHERE clause
                null,                                             // The values for the WHERE clause
                null,                                             // don't group the rows
                null,                                             // don't filter by row groups
                sortOrder,                                         // The sort order
                String.valueOf(limit)
        );
        if (cursor != null) {
            int count = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Transaction transaction = cursorToEntity(cursor);
                transactionList.add(transaction);
                cursor.moveToNext();
                count++;
            }
            cursor.close();
        }

        return transactionList;
    }

    private Transaction cursorToEntity(Cursor cursor) {

        String accountNo = null;
        ExpenseType expenseType = null;
        double amount = 0;
        Date date = null;
        int iaccountNo;
        int iexpenseType;
        int iamount;
        int idate;

        if (cursor != null) {
            if (cursor.getColumnIndex(ItransactionSchema.COLUMN_ACCOUNT_NO) != -1) {
                iaccountNo = cursor.getColumnIndex(ItransactionSchema.COLUMN_ACCOUNT_NO);
                accountNo = cursor.getString(iaccountNo);
                Log.d("account no",accountNo);
            }
            if (cursor.getColumnIndex(ItransactionSchema.COLUMN_EXPENSE_TYPE) != -1) {
                iexpenseType = cursor.getColumnIndexOrThrow(
                        ItransactionSchema.COLUMN_EXPENSE_TYPE);
                expenseType = ExpenseType.valueOf(cursor.getString(iexpenseType));
            }
            if (cursor.getColumnIndex(ItransactionSchema.COLUMN_AMOUNT) != -1) {
                iamount = cursor.getColumnIndexOrThrow(
                        ItransactionSchema.COLUMN_AMOUNT);
                amount = cursor.getDouble(iamount);
            }
            if (cursor.getColumnIndex(ItransactionSchema.COLUMN_DATE) != -1) {
                idate = cursor.getColumnIndexOrThrow(ItransactionSchema.COLUMN_DATE);
                date = new Date(cursor.getString(idate));
            }

        }
        return new Transaction(date,accountNo,expenseType,amount);
    }
}
