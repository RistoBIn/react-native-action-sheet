/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "ActionSheet.h"

#import <React/RCTBridge.h>
#import <React/RCTConvert.h>
#import <React/RCTLog.h>
#import <React/RCTUIManager.h>
#import <React/RCTUtils.h>
//
//#import <FBReactNativeSpec/FBReactNativeSpec.h>
//#import <RCTTypeSafety/RCTConvertHelpers.h>

#import "CoreModulesPlugins.h"

@interface ActionSheet () <UIActionSheetDelegate>
@end

@implementation ActionSheet {
  // Use NSMapTable, as UIAlertViews do not implement <NSCopying>
  // which is required for NSDictionary keys
  NSMapTable *_callbacks;
}

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

- (void)presentViewController:(UIViewController *)alertController
       onParentViewController:(UIViewController *)parentViewController
                anchorViewTag:(NSNumber *)anchorViewTag
{
  alertController.modalPresentationStyle = UIModalPresentationPopover;
  UIView *sourceView = parentViewController.view;

  if (anchorViewTag) {
    sourceView = [self.bridge.uiManager viewForReactTag:anchorViewTag];
  } else {
    alertController.popoverPresentationController.permittedArrowDirections = 0;
  }
  alertController.popoverPresentationController.sourceView = sourceView;
  alertController.popoverPresentationController.sourceRect = sourceView.bounds;
  [parentViewController presentViewController:alertController animated:YES completion:nil];
}

- (NSMutableArray<NSDictionary *> *) getButtons:(id)ops {
    NSMutableArray *res = [NSMutableArray array];
    NSArray *aOps = ops;
    
    for (int i = 0; i<[aOps count]; i++) {
        id item = aOps[i];
        NSDictionary * dic = item;
        if ([dic objectForKey:@"disabled"] != nil && dic[@"disabled"]) {
            [res addObject:@{ @"name" : dic[@"name"], @"disabled" : @YES}];
        } else {
            [res addObject:@{ @"name" : dic[@"name"], @"disabled" : @NO}];
        }
    }
    return res;
}

- (NSArray<NSNumber *> *) getDestructiveButtonIndices:(id)data {
    if (data) {
        NSArray *dd = data;
        NSMutableArray * res = [NSMutableArray array];
        for (int i = 0; i<[dd count]; i++) {
            [res addObject:[RCTConvert NSNumber:(dd[i] ? dd[i] : @-1)]];
        }
        return res;
    } else {
      NSNumber *destructiveButtonIndex = @-1;
      return @[ destructiveButtonIndex ];
    }
}

RCT_EXPORT_METHOD(showActionSheetWithOptions:(NSDictionary *)options callback:(RCTResponseSenderBlock)callback)
{
  if (RCTRunningInAppExtension()) {
    RCTLogError(@"Unable to show action sheet from app extension");
    return;
  }

  if (!_callbacks) {
    _callbacks = [NSMapTable strongToStrongObjectsMapTable];
  }

  NSString *title = options[@"title"];
  NSString *message = options[@"message"];
  NSMutableArray<NSDictionary *> *buttons = [self getButtons:options[@"options"]];
  NSInteger cancelButtonIndex = ([options objectForKey:@"cancelButtonIndex"] != nil) ? [RCTConvert double:options[@"cancelButtonIndex"]] : -1;
  NSArray<NSNumber *> *destructiveButtonIndices = [self getDestructiveButtonIndices:options[@"destructiveButtonIndices"]];
  NSNumber *anchor = [RCTConvert NSNumber:(options[@"tintColor"] ? options[@"anchor"] : nil)];
  UIColor *tintColor = [RCTConvert UIColor:(options[@"tintColor"] ? options[@"tintColor"] : nil)];

  UIViewController *controller = RCTPresentedViewController();
  

  if (controller == nil) {
    RCTLogError(@"Tried to display action sheet but there is no application window. options: %@", @{
      @"title" : title,
      @"message" : message,
      @"options" : buttons,
      @"cancelButtonIndex" : @(cancelButtonIndex),
      @"destructiveButtonIndices" : destructiveButtonIndices,
      @"anchor" : anchor,
      @"tintColor" : tintColor,
    });
    return;
  }

  /*
   * The `anchor` option takes a view to set as the anchor for the share
   * popup to point to, on iPads running iOS 8. If it is not passed, it
   * defaults to centering the share popup on screen without any arrows.
   */
  NSNumber *anchorViewTag = anchor;

  UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title
                                                                           message:message
                                                                    preferredStyle:UIAlertControllerStyleActionSheet];

  NSInteger index = 0;
  for (NSDictionary *button in buttons) {
    UIAlertActionStyle style = UIAlertActionStyleDefault;
    if ([destructiveButtonIndices containsObject:@(index)]) {
      style = UIAlertActionStyleDestructive;
    } else if (index == cancelButtonIndex) {
      style = UIAlertActionStyleCancel;
    }

    NSInteger localIndex = index;
    UIAlertAction * item = [UIAlertAction actionWithTitle:button[@"name"]
                                                      style:style
                                                    handler:^(__unused UIAlertAction *action) {
                                                      callback(@[ @(localIndex) ]);
    }];
    if ([button[@"disabled"]  isEqual: @YES]) {
      [item setEnabled:NO];
    }
    
    [alertController addAction:item];

    index++;
  }

  alertController.view.tintColor = tintColor;
#if defined(__IPHONE_OS_VERSION_MAX_ALLOWED) && defined(__IPHONE_13_0) && \
    __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_13_0
  if (@available(iOS 13.0, *)) {
    NSString *userInterfaceStyle = [RCTConvert NSString:options[@"userInterfaceStyle"]];

    if (userInterfaceStyle == nil || [userInterfaceStyle isEqualToString:@""]) {
      alertController.overrideUserInterfaceStyle = UIUserInterfaceStyleUnspecified;
    } else if ([userInterfaceStyle isEqualToString:@"dark"]) {
      alertController.overrideUserInterfaceStyle = UIUserInterfaceStyleDark;
    } else if ([userInterfaceStyle isEqualToString:@"light"]) {
      alertController.overrideUserInterfaceStyle = UIUserInterfaceStyleLight;
    }
  }
#endif

  [self presentViewController:alertController onParentViewController:controller anchorViewTag:anchorViewTag];
    
    //
    //    UIAlertController* alertController = [UIAlertController
    //               alertControllerWithTitle:@"title"
    //               message:@"message"
    //               preferredStyle:UIAlertControllerStyleActionSheet];
    //
    //            UIAlertAction* item = [UIAlertAction actionWithTitle:@"item"
    //               style:UIAlertActionStyleDefault
    //               handler:^(UIAlertAction *action) {
    //                //do something here
    //                //inform the selection to the WebView
    //
    //                [alertController dismissViewControllerAnimated:YES completion:nil];
    //            }];
    //
    //            UIAlertAction* cancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
    //                [alertController dismissViewControllerAnimated:YES completion:nil];
    //            }];
    //
    //    [item setEnabled:NO];
    //            [alertController addAction:item];
    //            [alertController addAction:cancelAction];
    //  [self presentViewController:alertController onParentViewController:controller anchorViewTag:nil];
}

@end
