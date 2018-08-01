package com.uonagent.supermagazin.utils.models

import android.os.Parcel
import android.os.Parcelable

data class OrderModel(
        var item: ItemModel = ItemModel(),
        var clientName: String = "",
        var clientEmail: String = "",
        var details: String = "",
        var uid: String = ""
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(ItemModel::class.java.classLoader),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(item, flags)
                parcel.writeString(clientName)
                parcel.writeString(clientEmail)
                parcel.writeString(details)
                parcel.writeString(uid)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<OrderModel> {
                override fun createFromParcel(parcel: Parcel): OrderModel {
                        return OrderModel(parcel)
                }

                override fun newArray(size: Int): Array<OrderModel?> {
                        return arrayOfNulls(size)
                }
        }
}