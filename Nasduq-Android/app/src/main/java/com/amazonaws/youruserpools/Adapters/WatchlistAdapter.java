package com.amazonaws.youruserpools.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.WatchlistItem;
import com.amazonaws.youruserpools.Fragments.WatchlistFragment;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Leandro on 3/19/2017.
 */

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {

    public List<WatchlistItem> watchlistItems;
    private WatchlistFragment fragment;
    protected Activity activity;

    public WatchlistAdapter(WatchlistFragment fragment, List<WatchlistItem> objects, Activity activity) {
        this.fragment = fragment;
        this.watchlistItems = objects;
        this.activity = activity;
    }

    @Override
    public WatchlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        switch (viewType) {

            case Constants.WATCHLIST_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_cell_watchlist, parent, false);

                return new WatchlistViewHolder(v);

            default:
                throw new RuntimeException("viewType not found for WatchlistAdapter");
        }
    }

    @Override
    public void onBindViewHolder(WatchlistAdapter.WatchlistViewHolder holder, int position) {

        WatchlistItem item = watchlistItems.get(position);
        holder.getItemViewType();
        holder.company_acronym.setText(item.getTicker());
        holder.company_name.setText(item.getCompanyName());
        holder.setActivity(activity);

        NumberFormat nf = NumberFormat.getInstance();
        Currency cur = Currency.getInstance(item.getCurrency());
        nf.setCurrency(cur);
        holder.currency.setText(cur.getSymbol());
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);


        String currency = nf.format(item.getPrice());
        holder.stock_value.setText(currency);

        double varNumValue = Math.abs(item.getChange());

        holder.variation_number_value.setText(String.format(
                Locale.US, "%.2f%%", varNumValue));

        holder.abs_change.setText(
                String.format(Locale.US, "(%s)", nf.format(item.getAbs_change())));

        if(item.getChange() > 0) {
            holder.stock_green_arrow.setImageResource(R.drawable.green_arrow);
            holder.variation_number_value.setTextColor(
                    ContextCompat.getColor(fragment.getContext(), R.color.dark_green));
            holder.abs_change.setTextColor(
                    ContextCompat.getColor(fragment.getContext(), R.color.dark_green));


        }
        else {
            holder.stock_red_arrow.setImageResource(R.drawable.red_arrow);
            holder.variation_number_value.setTextColor(Color.RED);
            holder.abs_change.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return watchlistItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return watchlistItems.get(position).itemType;
    }

    public class WatchlistViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        protected Activity activity;

        protected TextView company_acronym;
        protected TextView company_name;
        protected TextView stock_value;
        protected TextView variation_number_value;
        protected TextView currency;
        protected TextView abs_change;
        protected ImageView stock_green_arrow;
        protected ImageView stock_red_arrow;

        public WatchlistViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            company_acronym = (TextView) itemView.findViewById(R.id.company_acronym);
            company_name = (TextView) itemView.findViewById(R.id.company_name);
            stock_value = (TextView) itemView.findViewById(R.id.stock_value);
            variation_number_value = (TextView) itemView.findViewById(R.id.variation_number_value);
            currency = (TextView) itemView.findViewById(R.id.currency);
            abs_change = (TextView) itemView.findViewById(R.id.abs_change);
            stock_green_arrow = (ImageView) itemView.findViewById(R.id.variation_indicator);
            stock_red_arrow = (ImageView) itemView.findViewById(R.id.variation_indicator);

        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {

            WebView webView = (WebView) activity.findViewById(R.id.watchlist_webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl("https://www.google.com");

        }
    }
}
