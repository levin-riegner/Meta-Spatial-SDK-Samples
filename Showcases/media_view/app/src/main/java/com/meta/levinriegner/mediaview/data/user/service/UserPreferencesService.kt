// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.data.user.service

import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferencesService
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
) {

  fun isSampleMediaSaved(): Boolean {
    return sharedPreferences.getBoolean(KEY_IS_SAMPLE_MEDIA_SAVED, false)
  }

  fun setSampleMediaSaved(saved: Boolean) {
    sharedPreferences.edit().putBoolean(KEY_IS_SAMPLE_MEDIA_SAVED, saved).apply()
  }

  fun setOnboardingCompleted() {
    sharedPreferences.edit().putBoolean(KEY_IS_ONBOARDING_COMPLETED, true).apply()
  }

  fun isOnboardingCompleted(): Boolean {
    return sharedPreferences.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false)
  }

  companion object {
    private const val KEY_IS_SAMPLE_MEDIA_SAVED = "is_sample_media_saved"
    private const val KEY_IS_ONBOARDING_COMPLETED = "is_onboarding_completed"
  }
}
