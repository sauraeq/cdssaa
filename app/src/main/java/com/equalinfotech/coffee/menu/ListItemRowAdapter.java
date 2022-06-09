package com.equalinfotech.coffee.menu;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.equalinfotech.coffee.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListItemRowAdapter extends ArrayAdapter<ListItemRow> {
    private Context context;

    public ListItemRowAdapter(Context context, int resourceId,
                              List<ListItemRow> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView, iconarrow;
        TextView txtTitle;
        View view;
        RelativeLayout rel_list_view;
    }

    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        ViewHolder holder = null;
        ListItemRow rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item_row, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.textViewName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            holder.view = (View) convertView.findViewById(R.id.view1);
            holder.rel_list_view=convertView.findViewById(R.id.rel_list_view);
            //  holder.iconarrow = (ImageView) convertView.findViewById(R.id.iconarrow);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.txtTitle.setText(rowItem.getName());
        holder.imageView.setImageResource(rowItem.getIcon());


        ViewHolder finalHolder = holder;

        /*holder.rel_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0; i<parent.getChildCount(); i++)
                {
                    if(i == position)
                    {
                        parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                        finalHolder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
                        finalHolder.txtTitle.setTextColor(Color.BLACK);
                    }
                    else
                    {
                        parent.getChildAt(i).setBackgroundColor(Color.BLACK);
                        finalHolder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
                        finalHolder.txtTitle.setTextColor(Color.WHITE);
                    }

                }


            }
        });*/

        return convertView;

    }
}




