/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modeler.anchors;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;

/**
 * This class represents a node anchor used in P visualization style. The anchor
 * could be assign by multiple connection widgets. For each usage the anchor
 * resolves a different position. The positions are resolved at the top and the
 * bottom of the widget where the anchor is attached to.
 *
 * @author David Kaspar
 */
public class PNodeAnchor extends Anchor {

    private boolean requiresRecalculation = true;

    private HashMap<Entry, Result> results = new HashMap<Entry, Result>();
    private final boolean vertical;

    /**
     * Creates a node anchor.
     *
     * @param widget the node widget where the anchor is attached to
     * @param vertical if true, then anchors are placed vertically; if false,
     * then anchors are placed horizontally
     */
    public PNodeAnchor(Widget widget, boolean vertical) {
        super(widget);
        assert widget != null;
        this.vertical = vertical;
    }

    /**
     * Notifies when an entry is registered
     *
     * @param entry the registered entry
     */
    @Override
    protected void notifyEntryAdded(Entry entry) {
        requiresRecalculation = true;
    }

    /**
     * Notifies when an entry is unregistered
     *
     * @param entry the unregistered entry
     */
    @Override
    protected void notifyEntryRemoved(Entry entry) {
        results.remove(entry);
        requiresRecalculation = true;
    }

    /**
     * Notifies when the anchor is going to be revalidated.
     *
     * @since 2.8
     */
    @Override
    protected void notifyRevalidate() {
        requiresRecalculation = true;
    }

    private void recalculate() {
        if (!requiresRecalculation) {
            return;
        }

        Widget widget = getRelatedWidget();
        Point relatedLocation = getRelatedSceneLocation();

        Rectangle bounds = widget.convertLocalToScene(widget.getBounds());

        HashMap<Entry, Float> topmap = new HashMap<Entry, Float>();
        HashMap<Entry, Float> bottommap = new HashMap<Entry, Float>();

        for (Entry entry : getEntries()) {
            Point oppositeLocation = getOppositeSceneLocation(entry);
            if (oppositeLocation == null || relatedLocation == null) {
                results.put(entry, new Result(new Point(bounds.x, bounds.y), DIRECTION_ANY));
                continue;
            }

            int dy = oppositeLocation.y - relatedLocation.y;
            int dx = oppositeLocation.x - relatedLocation.x;

            if (vertical) {
                if (dy > 0) {
                    bottommap.put(entry, (float) dx / (float) dy);
                } else if (dy < 0) {
                    topmap.put(entry, (float) -dx / (float) dy);
                } else {
                    topmap.put(entry, dx < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
                }
            } else if (dx > 0) {
                bottommap.put(entry, (float) dy / (float) dx);
            } else if (dy < 0) {
                topmap.put(entry, (float) -dy / (float) dx);
            } else {
                topmap.put(entry, dy < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
            }
        }

        Entry[] topList = toArray(topmap);
        Entry[] bottomList = toArray(bottommap);
        int pinGap = 0;
        if (widget instanceof IPNodeWidget) {
            pinGap = ((IPNodeWidget) widget).getAnchorGap();
        } else if (widget instanceof IPinWidget) {
            pinGap = ((IPinWidget) widget).getAnchorGap();
        }
        int y = bounds.y - pinGap;
        int x = bounds.x - pinGap;
        int len = topList.length;

        for (int a = 0; a < len; a++) {
            Entry entry = topList[a];
            if (vertical) {
                x = bounds.x + (a + 1) * bounds.width / (len + 1);
            } else {
                y = bounds.y + (a + 1) * bounds.height / (len + 1);
            }
            results.put(entry, new Result(new Point(x, y), vertical ? Direction.TOP : Direction.LEFT));
        }

        y = bounds.y + bounds.height + pinGap;
        x = bounds.x + bounds.width + pinGap;
        len = bottomList.length;

        for (int a = 0; a < len; a++) {
            Entry entry = bottomList[a];
            if (vertical) {
                x = bounds.x + (a + 1) * bounds.width / (len + 1);
            } else {
                y = bounds.y + (a + 1) * bounds.height / (len + 1);
            }
            results.put(entry, new Result(new Point(x, y), vertical ? Direction.BOTTOM : Direction.RIGHT));
        }

        requiresRecalculation = false;
    }

    private Entry[] toArray(final HashMap<Entry, Float> map) {
        Set<Entry> keys = map.keySet();
        Entry[] entries = keys.toArray(new Entry[keys.size()]);
        Arrays.sort(entries, (Entry o1, Entry o2) -> {
            float f = map.get(o1) - map.get(o2);
            if (f > 0.0f) {
                return 1;
            } else if (f < 0.0f) {
                return -1;
            } else {
                return 0;
            }
        });
        return entries;
    }

    /**
     * Computes a result (position and direction) for a specific entry.
     *
     * @param entry the entry
     * @return the calculated result
     */
    @Override
    public Result compute(Entry entry) {
        recalculate();
        return results.get(entry);
    }

}
