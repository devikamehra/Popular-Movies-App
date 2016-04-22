package awe.devikamehra.popularmoviesapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import awe.devikamehra.popularmoviesapp.R;

/**
 * Created by Devika on 13-02-2016.
 */
// Custom Adapter for Spinner
public class SortOrderAdapter extends ArrayAdapter<String> {

    private ArrayList<String> data;

    public SortOrderAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.spinner_item, objects);
        data = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.spinner_text);
        textView.setText(data.get(position));
        return row;
    }
}