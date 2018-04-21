/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.modeler.analytics;


import org.analytics.Analytics;
import org.analytics.EventHit;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.NbBundle;

/**
 * Insight program to collect usage data event for developer experience
 * improvement. It does not intend to collect information that can uniquely
 * identify developer as an individual.
 *
 * @author jGauravGupta
 */
public class ILogger {

    private static Analytics ANALYTICS;
    private static boolean ENABLE = false;

    static {
        if (ENABLE = Boolean.valueOf(NbBundle.getMessage(ILogger.class, "TRACKING_ENABLE"))) {
            ANALYTICS = new Analytics(NbBundle.getMessage(ILogger.class, "TRACKING_ID"));
        }
    }

    private static final TemporalMap<String, Integer> data = new TemporalMap<String, Integer>(5, TimeUnit.MINUTES,
            (key, value) -> {
                String token[] = key.split("#");
                String category = token[0], action = token[1], label = token[2];
                EventHit eventHit = new EventHit(category, action, label, value);
                System.out.println("ANALYTICS category[" + category + "] action[" + action + "] label[" + label + "] value[" + value + "]");
                ANALYTICS.postAsync(eventHit);
            });

    public static void logEvent(String category, String action) {
        logEvent(category, action, null, 1);
    }

    public static void logEvent(String category, String action, String label) {
        logEvent(category, action, label, 1);
    }

    public static void logEvent(String category, String action, String label, int value) {
        if (ENABLE && StringUtils.isNotBlank(category) && StringUtils.isNotBlank(action) && StringUtils.isNotBlank(label)) {
            String key = category+"#"+action+"#"+label;
            Integer existingValue = data.get(key);
            if (existingValue != null) {
                data.update(key, value + existingValue);
            } else {
                data.save(key, value);
            }
        }
    }

}