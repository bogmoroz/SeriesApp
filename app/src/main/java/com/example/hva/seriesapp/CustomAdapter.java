package com.example.hva.seriesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bogdan on 3.10.2016.
 */

public class CustomAdapter extends BaseAdapter{


    private LayoutInflater inflater;
    private Context context;
    private List<Series> arrayList;

    public CustomAdapter(List<Series> list, Context c) {
        this.arrayList = list;
        this.context = c;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Series getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        ViewHolder holder;

        if (cell == null) {
            cell = inflater.inflate(R.layout.single_grid_item, parent, false);
            holder = new ViewHolder(cell);
            cell.setTag(holder);
        } else {
            holder = (ViewHolder) cell.getTag();
        }

        holder.populateCell(getItem(position));

        return cell;

    }

    class ViewHolder {
        private ImageView poster;
        private TextView title;

        //initialize variables
        public ViewHolder (View view) {
            poster = (ImageView) view.findViewById(R.id.poster);
            title = (TextView) view.findViewById(R.id.title);
        }

        public void populateCell (Series series) {
            poster.setImageResource(series.getImageSource());
            title.setText(series.getTitle());
        }
    }
}















