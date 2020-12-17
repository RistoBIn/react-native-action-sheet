package com.actionsheet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;

import java.util.List;

public class ButtonListAdapter extends ArrayAdapter {

    private Context context;
    private List<ButtonInfo> buttons;

    public ButtonListAdapter(Context context, List<ButtonInfo> buttons){
        super(context, R.layout.dialog_item, R.id.itemName, buttons);
        this.buttons = buttons;
        this.context = context;
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
        TextView            tvTitle;
        int                 position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView  = inflater.inflate(R.layout.dialog_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.itemName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ButtonInfo bi = buttons.get(position);

        viewHolder.tvTitle.setText(bi.getTitle());
        viewHolder.tvTitle.setTextColor(bi.isDisabled() ? Color.LTGRAY : Color.BLACK);

        viewHolder.position = position;
        return convertView;
    }
}
