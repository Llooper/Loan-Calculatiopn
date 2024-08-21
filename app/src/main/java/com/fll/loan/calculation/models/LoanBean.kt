package com.fll.loan.calculation.models

import android.os.Parcel
import android.os.Parcelable

data class LoanBean(
    val amount: Float = 0.0f,
    val fundAmount: Float = 0.0f,
    val rate: Float = 0.0f,
    val fundRate: Float = 0.0f,
    val firstPurchaseRate: Float = 0.0f,
    val yearCount: Int = 30,
    val area: Float = 0.0f,
    val perPrice: Float = 0.0f

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(amount)
        parcel.writeFloat(fundAmount)
        parcel.writeFloat(rate)
        parcel.writeFloat(fundRate)
        parcel.writeFloat(firstPurchaseRate)
        parcel.writeInt(yearCount)
        parcel.writeFloat(area)
        parcel.writeFloat(perPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanBean> {
        override fun createFromParcel(parcel: Parcel): LoanBean {
            return LoanBean(parcel)
        }

        override fun newArray(size: Int): Array<LoanBean?> {
            return arrayOfNulls(size)
        }
    }

}
