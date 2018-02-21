package minkush.com.sqlitedemoapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import minkush.com.sqlitedemoapp.AppController;
import minkush.com.sqlitedemoapp.InputScreenActivity;
import minkush.com.sqlitedemoapp.MainActivity;
import minkush.com.sqlitedemoapp.R;
import minkush.com.sqlitedemoapp.api.responseapi.ResponseRestCountriesApi;

import static minkush.com.sqlitedemoapp.InputScreenActivity.ACTION_INTENT;

/**
 * Created by wingify on 20/02/18.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder>{


    MainActivity mainActivity;
    ArrayList<String> countryIdArrayList;
    ArrayList<String> countryArrayList;
    ArrayList<String> capitalArrayList;
    ArrayList<String> regionArrayList;
    ArrayList<String> nativeNameArrayList;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class CountryHolder extends ViewHolder {

        TextView textView_name,textView_capital,textView_region,textView_nativename;
        Button button_edit,button_delete,button_share;
        public CountryHolder(View v) {
            super(v);
            textView_name = (TextView) v.findViewById(R.id.adapter_country_textview_name);
            textView_capital = (TextView) v.findViewById(R.id.adapter_country_textview_capital);
            textView_region = (TextView) v.findViewById(R.id.adapter_country_textview_region);
            textView_nativename = (TextView) v.findViewById(R.id.adapter_country_textview_nativename);
            button_edit = (Button) v.findViewById(R.id.adapter_country_button_edit);
            button_delete = (Button) v.findViewById(R.id.adapter_country_button_delete);
            button_share = (Button) v.findViewById(R.id.adapter_country_button_share);
        }
    }


    public CountriesAdapter(MainActivity mainActivity,ArrayList<String> countryIdArrayList,ArrayList<String> countryArrayList, ArrayList<String> capitalArrayList,
                            ArrayList<String> regionArrayList,ArrayList<String> nativeNameArrayList) {
        this.mainActivity = mainActivity;
        this.countryArrayList = countryArrayList;
        this.capitalArrayList = capitalArrayList;
        this.regionArrayList = regionArrayList;
        this.nativeNameArrayList = nativeNameArrayList;
        this.countryIdArrayList = countryIdArrayList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_country, viewGroup, false);
        return new CountryHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        CountryHolder countryHolder = (CountryHolder) viewHolder;

        countryHolder.textView_name.setText("Country Name : " + countryArrayList.get(position).toString());
        countryHolder.textView_capital.setText("Capital : "  + capitalArrayList.get(position).toString());
        countryHolder.textView_region.setText("Region : " + regionArrayList.get(position).toString());
        countryHolder.textView_nativename.setText("Native Name : " + nativeNameArrayList.get(position).toString());


        countryHolder.button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "Country Name : " + countryArrayList.get(position).toString() + "\n" +
                        "Capital : " + capitalArrayList.get(position).toString() + "\n" +
                        "Region : " + regionArrayList.get(position).toString() + "\n" +
                        "Native Name : " + nativeNameArrayList.get(position).toString();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Country Data");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mainActivity.startActivity(Intent.createChooser(sharingIntent,"Demo App"));
            }
        });
        countryHolder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity,InputScreenActivity.class);
                intent.putExtra(ACTION_INTENT, InputScreenActivity.ACTION_EDIT);
                intent.putExtra("countryId", countryIdArrayList.get(position).toString());
                intent.putExtra("country", countryArrayList.get(position).toString());
                intent.putExtra("capital",  capitalArrayList.get(position).toString());
                intent.putExtra("region", regionArrayList.get(position).toString());
                intent.putExtra("nativename",nativeNameArrayList.get(position).toString());
                mainActivity.startActivity(intent);
            }
        });

        countryHolder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mainActivity);
                builder1.setTitle("Alert");
                builder1.setMessage("Are you sure want to delete?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                AppController.getInstance().getSqliteInstance().deleteStudent(countryIdArrayList.get(position).toString());
                                Toast.makeText(mainActivity,"Delete Successfully",Toast.LENGTH_SHORT).show();
                                mainActivity.getDataFromSqlite();
                            }
                        });
                builder1.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                android.support.v7.app.AlertDialog alert11 = builder1.create();
                alert11.show();


            }

        });

    }

    @Override
    public int getItemCount() {

        return countryArrayList.size();

    }

    @Override
    public int getItemViewType(int position) {

        return 0;

    }

}
