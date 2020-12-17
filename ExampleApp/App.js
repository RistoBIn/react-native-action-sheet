import React from 'react';
import { View, SafeAreaView, TouchableOpacity, Text } from 'react-native';
import ActionSheet from 'react-native-action-sheet';

const processColor = require('react-native/Libraries/StyleSheet/processColor');

const App = () => {
    const showActionSheet = () => {
        var BUTTONSiOS = [
            { name: 'Option 0', disabled: true },
            { name: 'Option 1' },
            { name: 'Option 2', disabled: true },
            { name: 'Option 3' },
            { name: 'Cancel' }
        ];

        var BUTTONSandroid = [
            { name: 'Option 0', disabled: true },
            { name: 'Option 1', },
            { name: 'Option 2', },
            { name: 'Option 3', disabled: true },
        ];

        var DESTRUCTIVE_INDEX = 3;
        var CANCEL_INDEX = 4;
        ActionSheet.showActionSheetWithOptions({
            options: (Platform.OS == 'ios') ? BUTTONSiOS : BUTTONSandroid,
            cancelButtonIndex: CANCEL_INDEX,
            destructiveButtonIndex: DESTRUCTIVE_INDEX,
            tintColor: 'blue',
            title: "Hello"
        }, (buttonIndex) => {
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