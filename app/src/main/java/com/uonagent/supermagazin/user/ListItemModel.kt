package com.uonagent.supermagazin.user

import android.os.Parcel
import android.os.Parcelable

data class ListItemModel(
        var title: String = "",
        var cost: Double = Double.NaN,
        var description: String = "",
        var photo: String = "",
        var uid: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(cost)
        parcel.writeString(description)
        parcel.writeString(photo)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListItemModel> {
        override fun createFromParcel(parcel: Parcel): ListItemModel {
            return ListItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ListItemModel?> {
            return arrayOfNulls(size)
        }
    }
}