package com.example.tapfeedback;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class TapAccessibilityService extends AccessibilityService {
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || (event.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED &&
                event.getEventType() != AccessibilityEvent.TYPE_VIEW_LONG_CLICKED)) return;
        AccessibilityNodeInfo node = event.getSource();
        if (node == null) return;
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        node.recycle();
        if (bounds.width() <= 0 || bounds.height() <= 0) return;
        OverlayService overlay = OverlayService.instance;
        if (overlay != null) overlay.showAt(bounds.exactCenterX(), bounds.exactCenterY());
    }
    @Override public void onInterrupt() {}
}
