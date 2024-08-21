package com.fll.loan.calculation.ui.common

inline fun Float.format2(): Float {
    return "%.2f".format(this).toFloat()
}