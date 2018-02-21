package minkush.com.sqlitedemoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import minkush.com.sqlitedemoapp.database.SqliteController;

import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_CAPITAL;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_ID;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_NAME;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_NATIVENAME;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_REGION;

/**
 * Created by wingify on 20/02/18.
 */

public class InputScreenActivity extends AppCompatActivity {

    private AppCompatEditText appCompatEditText_countryname,appCompatEditText_capitalname,appCompatEditText_regionname,appCompatEditText_nativename;
    private AppCompatButton appCompatButton_submit;
    private SqliteController controller;
    public static String ACTION_INTENT = "isAddorEdit";
    public static String ACTION_EDIT = "edit";
    public static String ACTION_ADD = "add";

    private String TAG_ACTION;

    private String edit_countryId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputscreen);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatEditText_countryname = (AppCompatEditText)findViewById(R.id.activity_input_edittext_countryname);
        appCompatEditText_capitalname = (AppCompatEditText)findViewById(R.id.activity_input_edittext_capitalname);
        appCompatEditText_regionname = (AppCompatEditText)findViewById(R.id.activity_input_edittext_regionname);
        appCompatEditText_nativename = (AppCompatEditText)findViewById(R.id.activity_input_edittext_nativename);
        appCompatButton_submit = (AppCompatButton)findViewById(R.id.activity_input_button_submit);
        controller = AppController.getInstance().getSqliteInstance();

        Intent intent = getIntent();
        TAG_ACTION = intent.getStringExtra(ACTION_INTENT);
        if(TAG_ACTION.equals(ACTION_EDIT)){

            TAG_ACTION = ACTION_EDIT;
            edit_countryId = getIntent().getStringExtra("countryId");
            String county = getIntent().getStringExtra("country");
            String capital = getIntent().getStringExtra("capital");
            String region = getIntent().getStringExtra("region");
            String nativename = getIntent().getStringExtra("nativename");

            appCompatEditText_countryname.setText(county);
            appCompatEditText_capitalname.setText(capital);
            appCompatEditText_regionname.setText(region);
            appCompatEditText_nativename.setText(nativename);
            appCompatButton_submit.setText("Edit");

        }

        appCompatButton_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryname =   appCompatEditText_countryname.getText().toString();
                String capitalname =   appCompatEditText_capitalname.getText().toString();
                String regionname =   appCompatEditText_regionname.getText().toString();
                String nativename =   appCompatEditText_nativename.getText().toString();

                if(TextUtils.isEmpty(countryname)){
                    appCompatEditText_countryname.setError("Please enter country name");
                    return;
                }
                appCompatEditText_countryname.setError(null);

                if(TextUtils.isEmpty(capitalname)){
                    appCompatEditText_capitalname.setError("Please enter capital name");
                    return;
                }
                appCompatEditText_capitalname.setError(null);

                if(TextUtils.isEmpty(regionname)){
                    appCompatEditText_regionname.setError("Please enter region name");
                    return;
                }
                appCompatEditText_regionname.setError(null);

                if(TextUtils.isEmpty(nativename)){
                    appCompatEditText_nativename.setError("Please enter native name");
                    return;
                }
                appCompatEditText_nativename.setError(null);

                if(TAG_ACTION.equals(ACTION_EDIT)){
                    HashMap<String,String> stringHashMap = new HashMap<>();
                    stringHashMap.put(COLUMN_COUNTRY_NAME,countryname);
                    stringHashMap.put(COLUMN_COUNTRY_CAPITAL,capitalname);
                    stringHashMap.put(COLUMN_COUNTRY_REGION,regionname);
                    stringHashMap.put(COLUMN_COUNTRY_NATIVENAME,nativename);
                    int output = controller.updateStudent(edit_countryId,stringHashMap);
                    if(output == 1){
                        Toast.makeText(InputScreenActivity.this,"Data Update Successfully",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(InputScreenActivity.this,"Error!!",Toast.LENGTH_SHORT).show();
                    }

                    Log.i("output",""+output);
                }else if(TAG_ACTION.equals(ACTION_ADD)){
                     HashMap<String,String> stringHashMap = new HashMap<>();
                    stringHashMap.put(COLUMN_COUNTRY_NAME,countryname);
                    stringHashMap.put(COLUMN_COUNTRY_CAPITAL,capitalname);
                    stringHashMap.put(COLUMN_COUNTRY_REGION,regionname);
                    stringHashMap.put(COLUMN_COUNTRY_NATIVENAME,nativename);
                    controller.insertStudent(stringHashMap);
                    setResult(RESULT_OK);
                    Toast.makeText(InputScreenActivity.this,"Data Saved Successfully",Toast.LENGTH_SHORT).show();

                }

                finish();

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
