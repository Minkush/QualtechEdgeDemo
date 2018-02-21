package minkush.com.sqlitedemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import minkush.com.sqlitedemoapp.adapter.CountriesAdapter;
import minkush.com.sqlitedemoapp.api.postapi.PostRestCountriesApi;
import minkush.com.sqlitedemoapp.api.responseapi.ResponseRestCountriesApi;
import minkush.com.sqlitedemoapp.database.SqliteController;
import minkush.com.sqlitedemoapp.utility.InternetConnection;
import minkush.com.sqlitedemoapp.utility.SharePreference;

import static minkush.com.sqlitedemoapp.InputScreenActivity.ACTION_INTENT;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_CAPITAL;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_ID;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_NAME;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_NATIVENAME;
import static minkush.com.sqlitedemoapp.database.SqliteController.COLUMN_COUNTRY_REGION;
import static minkush.com.sqlitedemoapp.utility.APIUrlConstant.GET_REST_COUNTRIES_API;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CountriesAdapter countriesAdapter;
    private LinearLayoutManager mLayoutManager;

    SqliteController controller;
    ArrayList<String> countryIdArrayList = new ArrayList<>();
    ArrayList<String> countryArrayList = new ArrayList<>();
    ArrayList<String> capitalArrayList = new ArrayList<>();
    ArrayList<String> regionArrayList = new ArrayList<>();
    ArrayList<String> nativeNameArrayList = new ArrayList<>();
    ProgressDialog pDialog;
    public static int REQUEST_CODE_ADD_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        controller = AppController.getInstance().getSqliteInstance();

        initView();

    }

    public void initView(){
        pDialog = new ProgressDialog(this);
        countriesAdapter = new CountriesAdapter(this, countryIdArrayList, countryArrayList,
                capitalArrayList, regionArrayList, nativeNameArrayList);
        recyclerView.setAdapter(countriesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!InternetConnection.isInternetOn(this)) {
            Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show();
        }
        if (!InternetConnection.isInternetOn(this) || SharePreference.getKeyValueSharePreference(this,
                SharePreference.SharePrefrenceKeyConstant.s_key_twitter_access_token)) {
            getDataFromSqlite();
        } else {
            getDataFromApi();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_add, menu);
        return (super.onCreateOptionsMenu(menu));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, InputScreenActivity.class);
                intent.putExtra(ACTION_INTENT, InputScreenActivity.ACTION_ADD);
                startActivityForResult(intent, REQUEST_CODE_ADD_DATA);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    //post api
    private void getDataFromApi() {
        pDialog = new ProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Getting Data From Api...");
        pDialog.show();
        new PostRestCountriesApi(this).execute();
    }

    //get response from api
    public void responseRestCountriesApi(ResponseRestCountriesApi responseRestCountriesApi) {

        if (!responseRestCountriesApi.countryDataArrayList.isEmpty()) {
            SharePreference.saveKeyValueSharePreference(this, SharePreference.SharePrefrenceKeyConstant.s_key_twitter_access_token, true);
        }
        new AddDataToLocalSQLITE(responseRestCountriesApi.countryDataArrayList).execute();
    }

    // add api data to local database
    class AddDataToLocalSQLITE extends AsyncTask<Void, Void, String> {
        ArrayList<ResponseRestCountriesApi.CountryData> countryDataArrayList;
        public AddDataToLocalSQLITE(ArrayList<ResponseRestCountriesApi.CountryData> countryDataArrayList) {
            this.countryDataArrayList = countryDataArrayList;
        }
        @Override
        protected String doInBackground(Void... voids) {
            for (ResponseRestCountriesApi.CountryData countryData : countryDataArrayList) {
                HashMap<String, String> countryHashMap = new HashMap<>();
                countryHashMap.put(COLUMN_COUNTRY_NAME, countryData.name);
                countryHashMap.put(COLUMN_COUNTRY_CAPITAL, countryData.capital);
                countryHashMap.put(COLUMN_COUNTRY_REGION, countryData.region);
                countryHashMap.put(COLUMN_COUNTRY_NATIVENAME, countryData.nativeName);
                controller.insertCountry(countryHashMap);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            getDataFromSqlite();
        }
    }

    //get data from local database
    public void getDataFromSqlite() {

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        countryIdArrayList.clear();
        countryArrayList.clear();
        capitalArrayList.clear();
        regionArrayList.clear();
        nativeNameArrayList.clear();

        ArrayList<HashMap<String, String>> hashMapArrayList = controller.getAllCountries();
        for (HashMap<String, String> countryHashMap : hashMapArrayList) {
            countryIdArrayList.add(countryHashMap.get(COLUMN_COUNTRY_ID));
            countryArrayList.add(countryHashMap.get(COLUMN_COUNTRY_NAME));
            capitalArrayList.add(countryHashMap.get(COLUMN_COUNTRY_CAPITAL));
            regionArrayList.add(countryHashMap.get(COLUMN_COUNTRY_REGION));
            nativeNameArrayList.add(countryHashMap.get(COLUMN_COUNTRY_NATIVENAME));

        }
        countriesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_DATA) {
            if (resultCode == RESULT_OK) {
                recyclerView.scrollToPosition(countryIdArrayList.size());
            }
        }
    }
}
