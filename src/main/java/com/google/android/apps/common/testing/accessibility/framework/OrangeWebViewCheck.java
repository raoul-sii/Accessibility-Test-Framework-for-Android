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

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Check to ensure that the JavaScript is enabled on the WebView
 */
@TargetApi(11)
public class OrangeWebViewCheck extends AccessibilityViewHierarchyCheck {

  @Override
  public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

    List<AccessibilityViewCheckResult> results  = new ArrayList<AccessibilityViewCheckResult>(1);

    Set<View> viewsToCheck = ViewAccessibilityUtils.getAllViewsInHierarchy(root);

    for (View view : viewsToCheck) {
      if (view instanceof WebView) {
        WebView webView = (WebView) view;
        WebSettings webSettings = webView.getSettings();
        if (webSettings.getJavaScriptEnabled()) {
          results.add(new AccessibilityViewCheckResult(this.getClass(),
                  AccessibilityCheckResultType.ERROR,
                  "WebView must have the JavaScrit setting enabled.", webView));
        }
      } else {
        results.add(new AccessibilityViewCheckResult(this.getClass(),
                AccessibilityCheckResultType.NOT_RUN, "View must be a WebView", view));
      }
    }
    return results;
  }
}
