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

import android.arch.persistence.room.Room;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;
import com.google.android.apps.common.testing.accessibility.framework.database.AbbreviationDatabase;
import com.google.android.apps.common.testing.accessibility.framework.database.entity.AbbreviationFr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Check to ensure that a TextView with an abbreviation has a contentDescription
 */
public class OrangeAbbreviationTextContentDescViewCheck extends AccessibilityViewHierarchyCheck {

  @Override
  public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

    List<AccessibilityViewCheckResult> results  = new ArrayList<>(1);
    Set<View>                          allViews = ViewAccessibilityUtils.getAllViewsInHierarchy(root);
    AbbreviationDatabase database = Room.databaseBuilder(root.getContext(), AbbreviationDatabase.class, "abbreviation.db").allowMainThreadQueries().build();

    List<AbbreviationFr> abbreviations = database.abbreviationFrDao().getAll();
    if(abbreviations.isEmpty())
      database.abbreviationFrDao().insertAll(new AbbreviationFr("mr", "monsieur"), new AbbreviationFr("mme", "madame"));

    for(View view0 : allViews) {
      if (view0 instanceof TextView) {
        TextView textView = (TextView) view0;
        //If the text is not editable
        if ((textView.getEditableText() == null)) {
          if(!TextUtils.isEmpty(textView.getText())) {
            String text = textView.getText().toString();
            String contentDescription = TextUtils.isEmpty(textView.getContentDescription())? "": textView.getContentDescription().toString();
            for(AbbreviationFr abbreviation : database.abbreviationFrDao().getAll()) {
              if((text.contains(abbreviation.getAbbreviation()+" ") || text.contains(abbreviation.getAbbreviation()+".")) && TextUtils.isEmpty(textView.getContentDescription())
                      || (contentDescription.contains(abbreviation.getAbbreviation()+" ") || contentDescription.contains(abbreviation.getAbbreviation()+"."))) {
                results.add(new AccessibilityViewCheckResult(this.getClass(),
                        AccessibilityCheckResultType.ERROR,
                        "TextView with abbreviation must have a content description without abbreviation.",
                        textView));
                break;
              }
            }
          }
        } else {
          results.add(new AccessibilityViewCheckResult(this.getClass(),
                  AccessibilityCheckResultType.NOT_RUN, "TextView must not be editable", textView));
        }
      } else {
        results.add(new AccessibilityViewCheckResult(this.getClass(),
                AccessibilityCheckResultType.NOT_RUN, "View must be a TextView", view0));
      }
    }
    return results;
  }
}
