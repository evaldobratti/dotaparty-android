package me.dotaparty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import me.dotaparty.domain.Account;
import me.dotaparty.view.adapters.AccountAdapter;

public class Main extends AppCompatActivity {


    private ProgressBar searchProgBar;
    private ListView usersList;
    private EditText searchTxt;
    private Button searchBtn;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTxt = (EditText) findViewById(R.id.searchTxt);
        usersList = (ListView) findViewById(R.id.usersList);
        searchProgBar = (ProgressBar) findViewById(R.id.searchProBar);
        searchBtn = (Button) findViewById(R.id.searchBtn);

        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    search();
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Account account = (Account) parent.getItemAtPosition(position);
                new AlertDialog.Builder(Main.this)
                        .setTitle("Confirm")
                        .setMessage("You are going to identify yourself as " + account.getPersonaName() +
                        " and your matches will start to download.")
                        .setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Main.this, Profile.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No way", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .show();

            }
        });
    }

    private void search() {
        searchBtn.setVisibility(View.INVISIBLE);
        searchProgBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("query", searchTxt.getText().toString());
        HttpUtils.get("find", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Account> accs = Account.fromJSONArray(response.optJSONArray("accounts"));

                usersList.setAdapter(new AccountAdapter(Main.this, accs));

                searchBtn.setVisibility(View.VISIBLE);
                searchProgBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}
