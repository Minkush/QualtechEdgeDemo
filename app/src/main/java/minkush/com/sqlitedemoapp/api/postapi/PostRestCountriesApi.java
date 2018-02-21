package minkush.com.sqlitedemoapp.api.postapi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import minkush.com.sqlitedemoapp.MainActivity;
import minkush.com.sqlitedemoapp.api.responseapi.ResponseRestCountriesApi;

import static minkush.com.sqlitedemoapp.utility.APIUrlConstant.GET_REST_COUNTRIES_API;

/**
 * Created by minkush on 20/02/18.
 */

public class PostRestCountriesApi extends AsyncTask<Void, Void,String> {

    MainActivity mainActivity;

    private static final String TAG = PostRestCountriesApi.class.getSimpleName();

    public PostRestCountriesApi(final MainActivity mainActivity){
        this.mainActivity = mainActivity;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(GET_REST_COUNTRIES_API);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            Log.i(TAG,url.getPath());
            Log.i(TAG,""+responseCode);

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }



    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            ResponseRestCountriesApi responseRestCountriesApi = new ResponseRestCountriesApi(new JSONArray(response));
            mainActivity.responseRestCountriesApi(responseRestCountriesApi);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
