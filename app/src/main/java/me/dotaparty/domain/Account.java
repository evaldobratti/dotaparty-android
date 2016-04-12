package me.dotaparty.domain;

import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private JSONObject json;

    private Account(JSONObject json) {
        this.json = json;
    }

    public String getPersonaName() {
        return json.optJSONObject("current_update").optString("persona_name");
    }

    public String getAccountId() {
        return json.optString("account_id");
    }

    public String getUrlAvatarFull() {
        return json.optJSONObject("current_update").optString("url_avatar_full");
    }

    public static Account fromJSONObject(JSONObject json) {
        return new Account(json);
    }

    public static List<Account> fromJSONArray(JSONArray json) {
        List<Account> accs = new ArrayList<>();
        for (int i = 0; i < json.length(); i++)
            accs.add(fromJSONObject(json.optJSONObject(i)));
        return accs;
    }
}
