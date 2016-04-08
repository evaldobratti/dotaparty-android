package me.dotaparty;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchTxt = (EditText) findViewById(R.id.searchTxt);
        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        final ListView usersList = (ListView) findViewById(R.id.usersList);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.add("query", searchTxt.getText().toString());
                HttpUtils.get("find", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray accounts = (JSONArray)response.get("accounts");

                            final List<JSONObject> accs = new ArrayList<JSONObject>();
                            for (int i = 0; i < accounts.length(); i++) {
                                JSONObject acc = accounts.getJSONObject(i);
                                accs.add(acc);
                            }

                            usersList.setAdapter(new ArrayAdapter<JSONObject>(Main.this, 0, accs){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    Log.i("aff", "carregando " + position);

                                    JSONObject acc = getItem(position);
                                    // Check if an existing view is being reused, otherwise inflate the view
                                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_list_item, parent, false);
                                    TextView nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
                                    TextView accountIdTxt = (TextView) convertView.findViewById(R.id.accountIdTxt);
                                    ImageView avatarImg = (ImageView) convertView.findViewById(R.id.avatarImg);

                                    try {
                                        nameTxt.setText(acc.getJSONObject("current_update").getString("persona_name"));
                                        accountIdTxt.setText(acc.getString("account_id"));
                                        String avatarUrl = acc.getJSONObject("current_update").getString("url_avatar_full");

                                        new ImageViewCharger(avatarImg).execute(avatarUrl);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return convertView;
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }



}
