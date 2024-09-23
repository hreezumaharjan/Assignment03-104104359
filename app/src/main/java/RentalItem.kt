package au.edu.swin.sdmd.w06_myfirstintent

import android.os.Parcel
import android.os.Parcelable

data class RentalItem(
    val name: String,
    val rating: Float,
    val attribute: String,
    val pricePerMonth: String,
    val description: String,
    val imageResId: Int,
    var isBorrowed: Boolean = false // New field to track borrow status
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte() // Read isBorrowed as Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(rating)
        parcel.writeString(attribute)
        parcel.writeString(pricePerMonth)
        parcel.writeString(description)
        parcel.writeInt(imageResId)
        parcel.writeByte(if (isBorrowed) 1 else 0) // Write isBorrowed as Byte
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RentalItem> {
        override fun createFromParcel(parcel: Parcel): RentalItem {
            return RentalItem(parcel)
        }

        override fun newArray(size: Int): Array<RentalItem?> {
            return arrayOfNulls(size)
        }
    }
}
