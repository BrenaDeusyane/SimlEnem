package com.example.simulenem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private final List<CategoryModel> cat_list;

    public CategoryAdapter(List<CategoryModel> cat_list) {
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myView;

        if (convertView == null){
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout, parent, false);
        }else{
            myView = convertView;
        }

        myView.setOnClickListener((view) -> {

            DbQuery.google_selected_category_index = position;

            Intent intent = new Intent(view.getContext(), TestActivity.class);

            view.getContext().startActivity(intent);

        });

        TextView category_name = myView.findViewById(R.id.cat_name);
        TextView number_of_test = myView.findViewById(R.id.no_of_tests);

        category_name.setText(cat_list.get(position).getName());
        number_of_test.setText(cat_list.get(position).getNumOfTests() + " Questões");

        return myView;
    }
}
