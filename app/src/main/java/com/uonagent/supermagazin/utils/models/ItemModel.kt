package com.uonagent.supermagazin.utils.models

import android.os.Parcel
import android.os.Parcelable

data class ItemModel(
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
            parcel.readString())

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

    companion object CREATOR : Parcelable.Creator<ItemModel> {
        override fun createFromParcel(parcel: Parcel): ItemModel {
            return ItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemModel?> {
            return arrayOfNulls(size)
        }
    }

    fun asMap() = mapOf(
                Pair("title", title),
                Pair("cost", cost),
                Pair("description", description),
                Pair("photo", photo),
                Pair("uid", uid)
    )
}