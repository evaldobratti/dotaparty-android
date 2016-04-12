package me.dotaparty.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import me.dotaparty.R;
import me.dotaparty.domain.Account;

public class AccountAdapter extends ArrayAdapter<Account> {

    ImageLoader imageLoader = ImageLoader.getInstance();

    private static class ViewHolder {
        TextView nameTxt;
        TextView accountIdTxt;
        ImageView avatarImg;
    }

    public AccountAdapter(Context context, List<Account> accounts) {
        super(context, 0, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account acc = getItem(position);

        ViewHolder viewHolder;
        View viewToUse = convertView;
        if (viewToUse == null) {
            viewToUse = LayoutInflater.from(getContext()).inflate(R.layout.account_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameTxt = (TextView) viewToUse.findViewById(R.id.nameTxt);
            viewHolder.accountIdTxt = (TextView) viewToUse.findViewById(R.id.accountIdTxt);
            viewHolder.avatarImg = (ImageView) viewToUse.findViewById(R.id.avatarImg);

            viewToUse.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTxt.setText(acc.getPersonaName());
        viewHolder.accountIdTxt.setText(acc.getAccountId());
        imageLoader.displayImage(acc.getUrlAvatarFull(), viewHolder.avatarImg);

        return viewToUse;
    }
}
