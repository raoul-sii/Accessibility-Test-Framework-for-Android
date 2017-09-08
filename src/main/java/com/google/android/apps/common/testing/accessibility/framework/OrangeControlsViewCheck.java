/*
 * Copyright (C) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.android.apps.common.testing.accessibility.framework;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;

import java.util.ArrayList;
import java.util.List;


/**
 * Check to ensure that controls like Switch and CheckBox have an associated view with a labelFor attribute
 */
public class OrangeControlsViewCheck extends AccessibilityViewHierarchyCheck {

  @Override
  public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

    List<AccessibilityViewCheckResult> results  = new ArrayList<AccessibilityViewCheckResult>(1);

    for(View view : ViewAccessibilityUtils.getAllViewsInHierarchy(root)) {
      if(view instanceof Button
              || view instanceof Button
              || view instanceof EditText
              || view instanceof AutoCompleteTextView
              || view instanceof CheckBox
              || view instanceof RadioButton
              || view instanceof ToggleButton
              || view instanceof DatePicker
              || view instanceof TimePicker
              || view instanceof Spinner
              || view instanceof RadioGroup) {
        
          if(view instanceof TextView) {
            if (ViewAccessibilityUtils.isVisibleToUser(view) && !ViewAccessibilityUtils.hasText(view)) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "Controls must have a text or a content description.",
                      view));
            }
          } else {
            boolean hasAssociatedView = false;
            for(View viewIndex : ViewAccessibilityUtils.getAllViewsInHierarchy(root)) {
              if (viewIndex.getLabelFor() == view.getId()) {
                hasAssociatedView = true;
                break;
              }
            }
            if (ViewAccessibilityUtils.isVisibleToUser(view) && !ViewAccessibilityUtils.hasText(view) && !hasAssociatedView) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "Controls must have a content description or an associated view with 'labelFor' attribute.",
                      view));
            }
          }
          if(ViewAccessibilityUtils.shouldFocusView(view) && !view.isFocusable()) {
            results.add(new AccessibilityViewCheckResult(this.getClass(),
                    AccessibilityCheckResultType.ERROR,
                    "Controls must be focusable.",
                    view));
          }
      }
      else {
        results.add(new AccessibilityViewCheckResult(this.getClass(),
                AccessibilityCheckResultType.NOT_RUN, "View must be a control", view));
      }
    }
    return results;
  }
}
