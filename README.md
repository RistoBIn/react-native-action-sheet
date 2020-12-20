# react-native-action-sheet 

React native action sheet with native iOS and android (using the built-in [AlertDialog](https://developer.android.com/reference/android/app/AlertDialog.html) for android, [UIAlertController](https://developer.apple.com/documentation/uikit/uialertcontroller?language=objc) for iOS)

This component is a actionsheet component so that user can set "Disabled" button to ActionSheet. (iOS & Android)

## Usage

iOS | Android
------ | -------
<img title="iOS" src="https://i.ibb.co/QYYCY84/ios.png" height=550/> | <img title="Android" src="https://i.ibb.co/8bGBfL0/1608379266162.jpg" height=550/>

```javascript
import ActionSheet from 'react-native-action-sheet';
import { Platform } from 'react-native';

var BUTTONS = [
    { name: 'Option 0', disabled: true },
    { name: 'Option 1' },
    { name: 'Option 2', disabled: true },
    { name: 'Option 3' },
    { name: 'Cancel' }
];

var DESTRUCTIVE_INDEX = 3;
var CANCEL_INDEX = 4;

ActionSheet.showActionSheetWithOptions({
  options: BUTTONS,
  cancelButtonIndex: CANCEL_INDEX,
  destructiveButtonIndex: DESTRUCTIVE_INDEX,
  tintColor: 'blue',
  title: "Hello",
  message: "This component is ActionSheet with disabled button."
},
(buttonIndex) => {
  console.log('button clicked :', buttonIndex);
});
```

## Methods

For the iOS implementation see [ActionSheetIOS](https://facebook.github.io/react-native/docs/actionsheetios.html)

#### options

option | iOS  | Android | Info
------ | ---- | ------- | ----
options | OK | OK | (array of strings) - a list of button titles (required on iOS)
cancelButtonIndex | OK | - | (int) - index of cancel button in options (useless in android since we have back button)
destructiveButtonIndex | OK | - | (int) - index of destructive button in options (same as above)
title | OK | OK | (string) - a title to show above the action sheet
message | OK | - | (string) - a message to show below the title
tintColor | OK | - | (string) - a color to set to the text (defined by processColor)

## Author

Risto Binovski
