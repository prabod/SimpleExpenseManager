package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseManagerHelperDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.IAccountSchema;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.IAccountSchema.COLUMN_ID;

/**
 * Created by prabod on 11/20/16.
 */

public class PersistentAccountDAO implements AccountDAO {
    private ExpenseManagerHelperDB emDBHelper;
    private SQLiteDatabase db;
    public PersistentAccountDAO(Context context) {
        emDBHelper = new ExpenseManagerHelperDB(context);
        db = emDBHelper.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountList = new ArrayList<String>();
        String sortOrder =
                IAccountSchema.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                IAccountSchema.ACCOUNT_TABLE,                     // The table to query
                new String[]{IAccountSchema.COLUMN_ACCOUNT_NO},                   // The columns to return
                null,                                             // The columns for the WHERE clause
                null,                                             // The values for the WHERE clause
                null,                                             // don't group the rows
                null,                                             // don't filter by row groups
                sortOrder                                         // The sort order
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getColumnIndex(IAccountSchema.COLUMN_ACCOUNT_NO) != -1) {
                    int iaccountNo = cursor.getColumnIndexOrThrow(IAccountSchema.COLUMN_ACCOUNT_NO);
                    accountList.add(cursor.getString(iaccountNo));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return accountList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<Account>();
        String sortOrder =
                IAccountSchema.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                IAccountSchema.ACCOUNT_TABLE,                     // The table to query
                IAccountSchema.ACCOUNT_COLUMNS,                   // The columns to return
                null,                                             // The columns for the WHERE clause
                null,                                             // The values for the WHERE clause
                null,                                             // don't group the rows
                null,                                             // don't filter by row groups
                sortOrder                                         // The sort order
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Account account = cursorToEntity(cursor);
                accountList.add(account);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        final String selectionArgs[] = { String.valueOf(accountNo) };
        final String selection = IAccountSchema.COLUMN_ACCOUNT_NO + " = ?";
        Account account = null;
        Cursor cursor = db.query(
                IAccountSchema.ACCOUNT_TABLE,                     // The table to query
                IAccountSchema.ACCOUNT_COLUMNS,                   // The columns to return
                selection,                                        // The columns for the WHERE clause
                selectionArgs,                                    // The values for the WHERE clause
                null,                                             // don't group the rows
                null,                                             // don't filter by row groups
                null                                         // The sort order
        );
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                account = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(IAccountSchema.COLUMN_ACCOUNT_NO, account.getAccountNo());
        values.put(IAccountSchema.COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(IAccountSchema.COLUMN_BANK_NAME, account.getBankName());
        values.put(IAccountSchema.COLUMN_BALANCE, account.getBalance());
        long newRowId = db.insert(IAccountSchema.ACCOUNT_TABLE, null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        final String selectionArgs[] = { String.valueOf(accountNo) };
        final String selection = IAccountSchema.COLUMN_ACCOUNT_NO + " = ?";
        db.delete(IAccountSchema.ACCOUNT_TABLE, selection, selectionArgs);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account currentAcc = getAccount(accountNo);
        final String selectionArgs[] = { String.valueOf(accountNo) };
        final String selection = IAccountSchema.COLUMN_ACCOUNT_NO + " = ?";
        ContentValues values = new ContentValues();
        double balance = currentAcc.getBalance();
        if (expenseType == ExpenseType.INCOME){
            balance += amount;
        }
        else if (expenseType == ExpenseType.EXPENSE){
            balance -= amount;
        }
        values.put(IAccountSchema.COLUMN_BALANCE, balance);
        int count = db.update(
                IAccountSchema.ACCOUNT_TABLE,
                values,
                selection,
                selectionArgs);
    }

    private Account cursorToEntity(Cursor cursor) {

        String accountNo = null;
        String bankName = null;
        String accountHolderName = null;
        double balance = 0;
        int iaccountNo;
        int ibankName;
        int iaccountHolderName;
        int ibalance;

        if (cursor != null) {
            if (cursor.getColumnIndex(IAccountSchema.COLUMN_ACCOUNT_NO) != -1) {
                iaccountNo = cursor.getColumnIndexOrThrow(IAccountSchema.COLUMN_ACCOUNT_NO);
                accountNo = cursor.getString(iaccountNo);
            }
            if (cursor.getColumnIndex(IAccountSchema.COLUMN_BANK_NAME) != -1) {
                ibankName = cursor.getColumnIndexOrThrow(
                        IAccountSchema.COLUMN_BANK_NAME);
                bankName = cursor.getString(ibankName);
            }
            if (cursor.getColumnIndex(IAccountSchema.COLUMN_ACCOUNT_HOLDER_NAME) != -1) {
                iaccountHolderName = cursor.getColumnIndexOrThrow(
                        IAccountSchema.COLUMN_ACCOUNT_HOLDER_NAME);
                accountHolderName = cursor.getString(iaccountHolderName);
            }
            if (cursor.getColumnIndex(IAccountSchema.COLUMN_BALANCE) != -1) {
                ibalance = cursor.getColumnIndexOrThrow(IAccountSchema.COLUMN_BALANCE);
                balance = cursor.getDouble(ibalance);
            }

        }
        return new Account(accountNo, bankName,accountHolderName,balance);
    }
}
