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

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;

import java.util.ArrayList;
import java.util.List;


/**
 * Check to ensure that an editable TextView is not labeled by a contentDescription
 */
public class OrangeEditableContentDescViewCheck extends AccessibilityViewHierarchyCheck {

  @Override
  public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

    List<AccessibilityViewCheckResult> results  = new ArrayList<AccessibilityViewCheckResult>(1);

    for(View view0 : ViewAccessibilityUtils.getAllViewsInHierarchy(root)) {
      if (view0 instanceof TextView) {
        TextView textView = (TextView) view0;
        //If the text is not editable, null is returned.
        if ((textView.getEditableText() != null)) {
          if (!TextUtils.isEmpty(textView.getContentDescription())) {
            results.add(new AccessibilityViewCheckResult(this.getClass(),
                    AccessibilityCheckResultType.ERROR,
                    "Editable TextView should not have a contentDescription.", textView));
          }
          //check if a hint or an associated view with labelFor attribute exists
          boolean hasHint           = !TextUtils.isEmpty(textView.getHint());
          boolean hasAssociatedView = false;

          for(View view1 : ViewAccessibilityUtils.getAllViewsInHierarchy(root)) {
            if(view1.getLabelFor()==textView.getId()) {
              hasAssociatedView = true;
              break;
            }
          }
          if((hasHint && hasAssociatedView) || (!hasHint && !hasAssociatedView)) {
            results.add(new AccessibilityViewCheckResult(this.getClass(),
                    AccessibilityCheckResultType.ERROR,
                    "Editable TextView must have either a hint, either an associated view with labelFor attribute.",
                    textView));
          }
        } else {
          results.add(new AccessibilityViewCheckResult(this.getClass(),
                  AccessibilityCheckResultType.NOT_RUN, "TextView must be editable", textView));
        }
      } else {
        results.add(new AccessibilityViewCheckResult(this.getClass(),
                AccessibilityCheckResultType.NOT_RUN, "View must be a TextView", view0));
      }
    }
    return results;
  }
}
