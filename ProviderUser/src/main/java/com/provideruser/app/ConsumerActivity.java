package com.provideruser.app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumerActivity extends Activity implements View.OnClickListener {
    private EditText mEtInsertValue, mEtUpdateId, mEtUpdateValue, mEtDeleteId;
    private TextView mTvGetData;
    private Button mButtonInsert, mButtonUpdate, mButtonDelete, mButtonGetData;

    static final String AUTHORITY = "com.provider.birju.MainProvider";
    static final String prefix = "content://";
    static final String path = "provider_table";
    static final String URL = prefix + AUTHORITY + "/" + path;
    static final Uri CONTENT_URI = Uri.parse(URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    private void initializeViews() {
        mEtInsertValue = findViewById(R.id.et_insert_value);
        mEtUpdateId = findViewById(R.id.et_update_id);
        mEtUpdateValue = findViewById(R.id.et_update_value);
        mEtDeleteId = findViewById(R.id.et_delete_id);

        mTvGetData = findViewById(R.id.tv_get_data);

        mButtonInsert = findViewById(R.id.button_insert);
        mButtonUpdate = findViewById(R.id.button_update);
        mButtonDelete = findViewById(R.id.button_delete);
        mButtonGetData = findViewById(R.id.button_get);

        mButtonInsert.setOnClickListener(this);
        mButtonUpdate.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonGetData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_insert:
                String insertName = mEtInsertValue.getText().toString();
                //calling method to insert new value into database
                try {
                    if (insertNewData(insertName)) {
                        showToast("Successfully added new value.");
                    } else {
                        showToast("Value not inserted.");
                    }
                } catch (IllegalArgumentException e) {
                    showToast(e.getMessage());
                }
                break;

            case R.id.button_update:
                String updateId = mEtUpdateId.getText().toString();
                String updateName = mEtUpdateValue.getText().toString();
                //calling method updateData()
                try {
                    if (updateData(updateId, updateName)) {
                        showToast("Successfully updated the value.");
                    } else {
                        showToast("Entered Id does not exist in database.");
                    }
                } catch (IllegalArgumentException e) {
                    showToast(e.getMessage());
                }
                break;

            case R.id.button_delete:
                String deleteId = mEtDeleteId.getText().toString();
                //calling method to insert new value into database
                try {
                    if (deleteData(deleteId)) {
                        showToast("Successfully deleted.");
                    } else {
                        showToast("Entered id does not exist in database.");
                    }
                } catch (IllegalArgumentException e) {
                    showToast(e.getMessage());
                }
                break;

            case R.id.button_get:
                String records = getData();
                if (records != null && !records.equals("")) {
                    mTvGetData.setText(records);
                } else {
                    mTvGetData.setText("");
                    showToast("No record available.");
                }
                break;
        }
    }

    /*
    method to insert new value into database
    @param name
     */
    private boolean insertNewData(String name) {
        if (name == null || name.equals("") || name.startsWith(" ")) {
            throw new IllegalArgumentException("Name should be valid value.");
        } else {
            ContentValues values = new ContentValues();
            values.put("name", name);
            Uri uri = getContentResolver().insert(CONTENT_URI, values);
            if (uri == null)
                return false;
            else
                return true;
        }
    }

    /*
    method to update the name of a particular id
    @param id
    @param name
     */
    private boolean updateData(String id, String name) {
        if (id == null || id.equals("") || id.startsWith(" ") || name == null || name.equals("") || name.startsWith(" ")) {
            throw new IllegalArgumentException("Please enter valid value.");
        } else {
            int count = 0;
            ContentValues values = new ContentValues();
            values.put("name", name);
            String[] arr = {id};
            count = getContentResolver().update(CONTENT_URI, values, "id=?", arr);
            if (count == 0)
                return false;
            else
                return true;
        }
    }

    /*
    method to delete particular row
    @param id
     */
    private boolean deleteData(String id) {
        if (id == null || id.equals("") || id.startsWith(" ")) {
            throw new IllegalArgumentException("Invalid Id.");
        } else {
            int count = 0;
            count = getContentResolver().delete(CONTENT_URI, "id = " + id, null);
            if (count == 0)
                return false;
            else
                return true;
        }
    }

    /*
    method to get data from table
     */
    private String getData() {
        String[] arr = {"id", "name"};
        String[] selection = {"0", "iiiiii1111"};

        Cursor cursor = getContentResolver().query(CONTENT_URI, arr, "id>? and name!=?", selection, "id DESC");
        if (cursor != null) {
            cursor.moveToFirst();
            StringBuilder res = new StringBuilder();
            while (!cursor.isAfterLast()) {
                res.append("\n" + cursor.getString(cursor.getColumnIndex("id")) + "-" + cursor.getString(cursor.getColumnIndex("name")));
                cursor.moveToNext();
            }
            cursor.close();
            return "" + res;
        }
        return null;
    }

    /*
    method to show the toast
    @param message
     */
    private void showToast(String message) {
        Toast.makeText(ConsumerActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
