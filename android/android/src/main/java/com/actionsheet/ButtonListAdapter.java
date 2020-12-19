package com.actionsheet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;

import java.util.List;

public class ButtonListAdapter extends ArrayAdapter {

    private Context context;
    private List<ButtonInfo> buttons;
    private int tintColor;

    public ButtonListAdapter(Context context, List<ButtonInfo> buttons, int tintColor){
        super(context, R.layout.dialog_item, R.id.itemName, buttons);
        this.buttons = buttons;
        this.context = context;
        this.tintColor = tintColor;
    }

    public void setButtons(List<ButtonInfo> buttons) {
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    static class ViewHolder {
        LinearLayout        llItemContainer;
        TextView            tvTitle;
        RelativeLayout      rlDivider;
        int                 position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final ButtonInfo bi = buttons.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView  = inflater.inflate(R.layout.dialog_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.llItemContainer = (LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.rlDivider = (RelativeLayout) convertView.findViewById(R.id.divider);

            viewHolder.tvTitle.setText(bi.getTitle());

            if (position == 0) {
                viewHolder.tvTitle.setTextColor(Color.rgb(0x77, 0x77, 0x77));
                viewHolder.tvTitle.setPadding(20, 0, 20, 0);
                viewHolder.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            } else if (bi.isDisabled()) {
                viewHolder.tvTitle.setTextColor(Color.LTGRAY);
            } else if (bi.isDestructive()) {
                viewHolder.tvTitle.setTextColor(Color.RED);
            } else if (bi.isCancel()) {
                viewHolder.tvTitle.setTextColor(this.tintColor);
                viewHolder.tvTitle.setTypeface(null, Typeface.BOLD);
            } else {
                viewHolder.tvTitle.setTextColor(this.tintColor);
            }

            if (position == 0 || position == buttons.size() - 1) {
                viewHolder.rlDivider.setVisibility(View.GONE);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;
        return convertView;
    }
}
