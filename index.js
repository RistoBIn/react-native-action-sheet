import { NativeModules, Platform } from 'react-native';

const { ActionSheet } = NativeModules;

const processColor = require('react-native/Libraries/StyleSheet/processColor');
const invariant = require('invariant');

export default (Platform.OS === 'ios') ? {
    showActionSheetWithOptions: (options, callback) => {
        invariant(
            typeof options === 'object' && options !== null,
            'Options must be a valid object',
        );
        invariant(typeof callback === 'function', 'Must provide a valid callback');
        invariant(ActionSheet, "ActionSheetManager does't exist");

        const { tintColor, destructiveButtonIndex, ...remainingOptions } = options;
        let destructiveButtonIndices = null;

        if (Array.isArray(destructiveButtonIndex)) {
            destructiveButtonIndices = destructiveButtonIndex;
        } else if (typeof destructiveButtonIndex === 'number') {
            destructiveButtonIndices = [destructiveButtonIndex];
        }

        const processedTintColor = processColor(tintColor);
        invariant(
            processedTintColor == null || typeof processedTintColor === 'number',
            'Unexpected color given for ActionSheetIOS.showActionSheetWithOptions tintColor',
        );
        ActionSheet.showActionSheetWithOptions(
            {
                ...remainingOptions,
                tintColor: processedTintColor,
                destructiveButtonIndices,
            },
            callback,
        );
    }
} : ActionSheet;
