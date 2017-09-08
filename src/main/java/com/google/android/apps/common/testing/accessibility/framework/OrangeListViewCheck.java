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

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Check to ensure that each List item is:
 * - focusable
 * - has a none empty contentDescription
 */
public class OrangeListViewCheck extends AccessibilityViewHierarchyCheck {

  @Override
  public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(View root) {

    List<AccessibilityViewCheckResult> results  = new ArrayList<AccessibilityViewCheckResult>(1);

    Set<View> viewsToCheck = ViewAccessibilityUtils.getAllViewsInHierarchy(root);

    for (View view : viewsToCheck) {
      if (view instanceof ListView) {
        ListView listView = (ListView) view;
        ListAdapter adapter = listView.getAdapter();
        if (adapter!=null) {
          for (int i = 0; i < adapter.getCount(); i++) {
            View viewItem = adapter.getView(i, null, null);
            if(!viewItem.isFocusable()) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "Item of ListView must be focusable.", viewItem));
            }
            if(TextUtils.isEmpty(viewItem.getContentDescription())) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "Item of ListView must have a content description.", viewItem));
            }
          }
        }
      } else if (view instanceof RecyclerView) {
        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter!=null) {
          for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
              if(viewHolder.itemView.getFocusable()!=View.FOCUSABLE) {
                results.add(new AccessibilityViewCheckResult(this.getClass(),
                        AccessibilityCheckResultType.ERROR,
                        "RecyclerView item must be focusable.", viewHolder.itemView));
              }
            }
            if(TextUtils.isEmpty(viewHolder.itemView.getContentDescription())) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "RecyclerView item must have a content description.", viewHolder.itemView));
            }
          }
        }
      } else if (view instanceof ExpandableListView) {
        ExpandableListView    expandableListView    = (ExpandableListView) view;
        ExpandableListAdapter expandableListAdapter = expandableListView.getExpandableListAdapter();
        if (expandableListAdapter!=null) {
          for (int groupIndex = 0; groupIndex < expandableListAdapter.getGroupCount(); groupIndex++) {
            boolean isExpanded = false;
            //Check the contentDescription of each group for each state (collapsed/expanded)
            View groupView = expandableListAdapter.getGroupView(groupIndex, isExpanded, null, null);
            expandableListView.expandGroup(groupIndex);
            CharSequence expandedContentDescription = groupView.getContentDescription();
            expandableListView.collapseGroup(groupIndex);
            CharSequence collapsedContentDescription = groupView.getContentDescription();
            if(TextUtils.isEmpty(expandedContentDescription) || TextUtils.isEmpty(collapsedContentDescription) || TextUtils.equals(expandedContentDescription, collapsedContentDescription)) {
              results.add(new AccessibilityViewCheckResult(this.getClass(),
                      AccessibilityCheckResultType.ERROR,
                      "Group of ExpandableListView must have a specific content description for each expanded/collapsed state.", groupView));
            }

            //Check the contentDescription and the focus of each child item for each state (selected/unselected)
            for(int childIndex = 0; childIndex < expandableListAdapter.getChildrenCount(groupIndex); childIndex++) {
              boolean isLast = false;
              View viewItem = expandableListAdapter.getChildView(groupIndex, childIndex, isLast, null, null);
              if (!viewItem.isFocusable() && view.isEnabled()) {
                results.add(new AccessibilityViewCheckResult(this.getClass(),
                        AccessibilityCheckResultType.ERROR,
                        "Item of ExpandableListView must be focusable.", viewItem));
              }
              if(expandableListAdapter.isChildSelectable(groupIndex, childIndex)) {
                CharSequence unselectedContentDescription = viewItem.getContentDescription();
                expandableListView.setSelectedChild(groupIndex, childIndex, true);
                CharSequence selectedContentDescription = viewItem.getContentDescription();
                if (TextUtils.isEmpty(unselectedContentDescription) || TextUtils.isEmpty(selectedContentDescription) || TextUtils.equals(unselectedContentDescription, selectedContentDescription)) {
                  results.add(new AccessibilityViewCheckResult(this.getClass(),
                          AccessibilityCheckResultType.ERROR,
                          "Item of ExpandableListView Group must have a content description for each selected/unselected state.", viewItem));
                }
              } else {
                if (TextUtils.isEmpty(viewItem.getContentDescription())) {
                  results.add(new AccessibilityViewCheckResult(this.getClass(),
                          AccessibilityCheckResultType.ERROR,
                          "Item of ExpandableListView must have a content description.", viewItem));
                }
              }
            }
          }
        }
      } else {
        results.add(new AccessibilityViewCheckResult(this.getClass(),
                AccessibilityCheckResultType.NOT_RUN, "View must be a ListView, a RecyclerView or an ExpandableListView", view));
      }
    }
    return results;
  }
}
