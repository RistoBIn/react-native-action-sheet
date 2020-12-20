import React from 'react';
import { View, SafeAreaView, TouchableOpacity, Text } from 'react-native';
import ActionSheet from 'react-native-action-sheet';

const App = () => {
    const showActionSheet = () => {
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
    }

    return (
        <SafeAreaView style={{ flex: 1 }}>
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <TouchableOpacity onPress={showActionSheet}>
                    <Text style={{ fontSize: 20, color: 'blue' }}>Open sheet</Text>
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    )
}

export default App;