package minkush.com.sqlitedemoapp.api.responseapi;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by minkush on 20/02/18.
 */


public class ResponseRestCountriesApi {

    public ArrayList<CountryData> countryDataArrayList = new ArrayList<>();

    public ResponseRestCountriesApi(JSONArray jsonArray) {
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                countryDataArrayList.add(new CountryData(jsonArray.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class CountryData {

        public String name, capital, region, nativeName;
        public String callingCodes;

        public CountryData(JSONObject jsonObject) {
            try {

                name = jsonObject.getString("name");
                capital = jsonObject.getString("capital");
                region = jsonObject.getString("region");
                nativeName = jsonObject.getString("nativeName");
                JSONArray jAr_callingCodes = jsonObject.getJSONArray("callingCodes");
                callingCodes = "";
                for(int i = 0 ; i < jAr_callingCodes.length() ; i++){
                    callingCodes = callingCodes + jAr_callingCodes.getString(i) + " ";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}