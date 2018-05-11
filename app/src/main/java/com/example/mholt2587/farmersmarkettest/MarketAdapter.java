package com.example.mholt2587.farmersmarkettest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mholt2587 on 4/19/2018.
 */

public class MarketAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private Market[] mDataSource;

    public MarketAdapter(Context context, Market[] items){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public int getCount() {
        return mDataSource.length;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_market, parent, false);
        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.market_list_title);

// Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.market_list_subtitle);

// Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.market_list_detail);

// Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.market_list_thumbnail);

        Market market = (Market) getItem(position);

        titleTextView.setText(market.getMarketname());
        subtitleTextView.setText(String.valueOf(market.getMarketAddress()));
       detailTextView.setText(market.getId());

       // Picasso.with(mContext).load(restaurant.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }
}

