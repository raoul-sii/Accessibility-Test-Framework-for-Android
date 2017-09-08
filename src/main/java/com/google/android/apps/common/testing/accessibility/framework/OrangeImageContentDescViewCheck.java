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

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Check to ensure that an image has a contentDescription attribute at least null
 */
public class OrangeImageContentDescViewCheck extends AccessibilityViewHierarchyCheck {
        @Override
        public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

            List<AccessibilityViewCheckResult> results  = new ArrayList<>();
            Set<View>                          allViews = ViewAccessibilityUtils.getAllViewsInHierarchy(root);

            for(View view : allViews) {
                if (view instanceof ViewGroup) {
                    @LayoutRes
                    int layoutId = view.getId();
                    if(layoutId != View.NO_ID) {
                        Resources         resources         = view.getResources();
                        XmlResourceParser xmlResourceParser = resources.getLayout(layoutId);

                        try {
                            int eventType = xmlResourceParser.getEventType();
                            while (eventType != XmlResourceParser.END_DOCUMENT) {
                                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG) {
                                    String tagName = xmlResourceParser.getName();
                                    if (tagName.contentEquals("ImageView") || tagName.contentEquals("ImageButton")) {
                                        if (TextUtils.isEmpty(xmlResourceParser.getAttributeValue(null, "contentDescription")) && xmlResourceParser.getAttributeBooleanValue(null, "importantForAccessibility", true)) {
                                            results.add(new AccessibilityViewCheckResult(this.getClass(),
                                                    AccessibilityCheckResultType.ERROR,
                                                    "ImageView and ImageButton should have a contentDescription at least equal to '@null'.", view));
                                        }
                                    }
                                }
                                xmlResourceParser.nextTag();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            xmlResourceParser.close();
                        }
                    }
                } else {
                    results.add(new AccessibilityViewCheckResult(this.getClass(),
                            AccessibilityCheckResultType.NOT_RUN, "View must be a ViewGroup", view));
                }
            }
            return results;
        }
}
