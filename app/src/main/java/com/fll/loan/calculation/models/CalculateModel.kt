package com.fll.loan.calculation.models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.fll.loan.calculation.SettingsActivity.Companion.COM_RATE_DEFAULT
import com.fll.loan.calculation.SettingsActivity.Companion.FUND_RATE_DEFAULT
import com.fll.loan.calculation.SettingsActivity.Companion.SP_COM_RATE_BASELINE
import com.fll.loan.calculation.SettingsActivity.Companion.SP_FUND_RATE_BASELINE
import com.fll.loan.calculation.ui.common.format2

class CalculateModel(private val sp: SharedPreferences) : ViewModel() {
    fun getCommercialRateLists(): ArrayList<String> {
        val comRateBaseline = sp.getFloat(SP_COM_RATE_BASELINE, COM_RATE_DEFAULT)
        return ArrayList<String>().apply {
            for (i in 0 until 7) {
                this.add(((0.10 * i + 0.70) * comRateBaseline).toFloat().format2().toString())
            }
        }
    }

    fun getFundRateLists(): ArrayList<String> {
        val fundRateBaseline = sp.getFloat(SP_FUND_RATE_BASELINE, FUND_RATE_DEFAULT)
        return ArrayList<String>().apply {
            for (i in 0 until 7) {
                this.add(((0.10 * i + 0.70) * fundRateBaseline).toFloat().format2().toString())
            }
        }
    }

    fun getDateLists(): ArrayList<String> {
        return ArrayList<String>().apply {
            for (i in 0 until 6) {
                this.add((5 * i + 5).toString())
            }
        }
    }

    fun getPercentLists(): ArrayList<String> {
        return ArrayList<String>().apply {
            for (i in 0 until 8) {
                this.add((i + 3).toString())
            }
        }
    }
}