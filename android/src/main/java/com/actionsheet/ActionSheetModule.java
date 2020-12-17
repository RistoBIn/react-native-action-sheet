package com.actionsheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

    final List<ButtonInfo> buttons = new ArrayList<ButtonInfo>();

    if (options.hasKey("options")) {
      ReadableArray customButtons = options.getArray("options");
      for (int i = 0; i < customButtons.size(); i++) {
        int currentIndex = buttons.size();

        ButtonInfo bi = new ButtonInfo();
        bi.setTitle(customButtons.getMap(i).getString("name"));
        bi.setDisabled((customButtons.getMap(i).hasKey("disabled") &&
                customButtons.getMap(i).getBoolean("disabled")) ? true : false);
        buttons.add(currentIndex, bi);
      }
    }

//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentActivity,
//            R.layout.dialog_item, titles);
    AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity, R.style.DialogStyle);
    if (options.hasKey("title") && options.getString("title") != null && !options.getString("title").isEmpty()) {
      builder.setTitle(options.getString("title"));
    }

    /* @TODO message currently disable the options
    if (options.hasKey("message") && options.getString("message") != null && !options.getString("message").isEmpty()) {
      builder.setMessage(options.getString("message"));
    }
    */

    ButtonListAdapter adapter = new ButtonListAdapter(currentActivity, buttons);
    builder.setAdapter(adapter, null);

    final AlertDialog dialog = builder.create();

    dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (buttons.get(position).isDisabled()) {

        } else {
          callback.invoke(position);
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
    dialog.show();
  }
}
