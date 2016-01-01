/*
 * Copyright (C) 2015 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dirtyunicorns.dutweaks.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;
import com.android.internal.util.du.DeviceUtils;

public class StatusbarNotifications extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String FORCE_EXPANDED_NOTIFICATIONS = "force_expanded_notifications";
    private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
    private static final String STATUS_BAR_NOTIF_COUNT = "status_bar_notif_count";
    private static final String MISSED_CALL_BREATH = "missed_call_breath";
    private static final String VOICEMAIL_BREATH = "voicemail_breath";
    private static final String BREATHING_NOTIFICATIONS = "breathing_notifications";

    private SwitchPreference mForceExpanded;
    private SwitchPreference mDisableIM;
    private SwitchPreference mEnableNC;
    private SwitchPreference mMissedCallBreath;
    private SwitchPreference mVoicemailBreath;
    private PreferenceGroup mBreathingNotifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_notifications);
        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();

        mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setChecked((Settings.System.getInt(resolver, Settings.System.DISABLE_IMMERSIVE_MESSAGE, 0) == 1));

	mForceExpanded = (SwitchPreference) findPreference(FORCE_EXPANDED_NOTIFICATIONS);
        mForceExpanded.setChecked((Settings.System.getInt(resolver, Settings.System.FORCE_EXPANDED_NOTIFICATIONS, 0) == 1));

	mEnableNC = (SwitchPreference) findPreference(STATUS_BAR_NOTIF_COUNT);
        mEnableNC.setChecked((Settings.System.getInt(resolver, Settings.System.STATUS_BAR_NOTIF_COUNT, 0) == 1));

        mMissedCallBreath = (SwitchPreference) findPreference(MISSED_CALL_BREATH);
        mVoicemailBreath = (SwitchPreference) findPreference(VOICEMAIL_BREATH);

        mBreathingNotifications = (PreferenceGroup) findPreference(BREATHING_NOTIFICATIONS);

        if (!DeviceUtils.deviceSupportsMobileData(getActivity())) {
            getPreferenceScreen().removePreference(findPreference(MISSED_CALL_BREATH));
            getPreferenceScreen().removePreference(findPreference(VOICEMAIL_BREATH));
            getPreferenceScreen().removePreference(findPreference(BREATHING_NOTIFICATIONS));
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if  (preference == mForceExpanded) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FORCE_EXPANDED_NOTIFICATIONS, checked ? 1:0);
            return true;
        }
        if  (preference == mDisableIM) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_IMMERSIVE_MESSAGE, checked ? 1:0);
            return true;
        }
        if  (preference == mEnableNC) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_NOTIF_COUNT, checked ? 1:0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mMissedCallBreath) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(resolver, Settings.System.KEY_MISSED_CALL_BREATH, value ? 1 : 0);
        } else if (preference == mVoicemailBreath) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(resolver, Settings.System.KEY_VOICEMAIL_BREATH, value ? 1 : 0);
        } else {
            return false;
        }

        return true;
    }
}
