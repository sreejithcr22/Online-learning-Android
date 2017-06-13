package com.codit.interview.aptitude;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sreejith on 27-Jul-16.
 */
public class NavListAdapter extends ArrayAdapter<NavListRow> {

    Context context;
    int layoutResourceId;
    NavListRow listItems[];




    public NavListAdapter(Context context, int resource, NavListRow[] objects) {
        super(context, resource, objects);

        this.context=context;
        this.layoutResourceId=resource;
        this.listItems=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
               context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(layoutResourceId, parent, false);



        ImageView imageView=(ImageView)listItem.findViewById(R.id.nav_icon);
        TextView label=(TextView)listItem.findViewById(R.id.label);

        if(layoutResourceId==R.layout.settings_nav_row) {
            int height = getContext().getResources().getDisplayMetrics().heightPixels/(listItems.length+1);

            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            listItem.setLayoutParams(params);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(height*.4)));
        }

        NavListRow object=listItems[position];

        imageView.setImageResource(object.getIcon());

        label.setText(object.getLabel());

        if(layoutResourceId==R.layout.menu_nav_row && APPSTATE.CURRENT_SELECTED_DRAWER_ITEM==position+1)
        {
            if(APPSTATE.CURRENT_THEME==APPSTATE.THEME_DEFAULT)
            {
                label.setTextColor(Color.parseColor("#43a047"));
                listItem.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
            else
            {
                label.setTextColor(Color.parseColor("#00acc1"));
                listItem.setBackgroundColor(Color.parseColor("#455A64"));


            }
        }



        return listItem;




    }


}
