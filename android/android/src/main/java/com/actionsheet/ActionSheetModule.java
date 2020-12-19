package com.actionsheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.util.List;
import java.util.ArrayList;

public class ActionSheetModule extends ReactContextBaseJavaModule {
  WritableMap response;

  public ActionSheetModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ActionSheet";
  }

  @ReactMethod
  public void showActionSheetWithOptions(final ReadableMap options, final Callback callback) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      response = Arguments.createMap();
      response.putString("error", "can't find current Activity");
      callback.invoke(response);
      return;
    }

    final List<Integer> destructiveButtons = new ArrayList<Integer>();
    final List<ButtonInfo> buttons = new ArrayList<ButtonInfo>();
    final Integer cancelBtnIndex = options.getInt("cancelButtonIndex");
    final int tintColor = options.hasKey("tintColor") ? options.getInt("tintColor") : Color.BLACK;

    if (options.hasKey("destructiveButtonIndices")) {
      ReadableArray desButtons = options.getArray("destructiveButtonIndices");
      for (int i = 0; i < desButtons.size(); i++) {
        destructiveButtons.add(desButtons.getInt(i));
      }
    }

    buttons.add(new ButtonInfo(options.getString("message"), true));

    if (options.hasKey("options")) {
      ReadableArray customButtons = options.getArray("options");
      for (int i = 0; i < customButtons.size(); i++) {
        ButtonInfo bi = new ButtonInfo();
        bi.setTitle(customButtons.getMap(i).getString("name"));
        bi.setDisabled((customButtons.getMap(i).hasKey("disabled") &&
                customButtons.getMap(i).getBoolean("disabled")) ? true : false);

        if (destructiveButtons.indexOf(i) >= 0) {
          bi.setDestructive(true);
        }

        if (i == cancelBtnIndex) {
          bi.setCancel(true);
        }
        buttons.add(bi);
      }
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity, R.style.DialogStyle);
    if (options.hasKey("title") && options.getString("title") != null && !options.getString("title").isEmpty()) {
      builder.setTitle(options.getString("title"));
    }

    /* @TODO message currently disable the options
    if (options.hasKey("message") && options.getString("message") != null && !options.getString("message").isEmpty()) {
      builder.setMessage(options.getString("message"));
    }
    */

    ButtonListAdapter adapter = new ButtonListAdapter(currentActivity, buttons, tintColor);
    builder.setAdapter(adapter, null);

    final AlertDialog dialog = builder.create();

    dialog.getListView().setDividerHeight(5);
    dialog.getListView().setDivider(new ColorDrawable(Color.parseColor("#FF000000")));
    dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {

        } else if (buttons.get(position).isDisabled()) {

        } else {
          callback.invoke(position - 1);
          dialog.dismiss();
        }
      }
    });

    /**
     * override onCancel method to callback cancel in case of a touch outside of
     * the dialog or the BACK key pressed
     */
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        Integer index = null;

        if (options.hasKey("cancelButtonIndex")) {
          index = options.getInt("cancelButtonIndex");
        }

        dialog.dismiss();
        callback.invoke(index);
      }
    });

    WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
    wmlp.copyFrom(dialog.getWindow().getAttributes());
    wmlp.gravity = Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT;

    dialog.show();
  }
}
