package com.zywczas.bestonscreen.utilities

import android.os.Parcel
import android.os.Parcelable
import com.zywczas.bestonscreen.model.Movie
import java.util.ArrayList

class PairMoviesInt<out E, out T>(val first: ArrayList<Movie>, val second: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Movie) as ArrayList<Movie>,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(first)
        parcel.writeInt(second)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PairMoviesInt<ArrayList<Movie>, Int>> {
        override fun createFromParcel(source: Parcel): PairMoviesInt<ArrayList<Movie>, Int> {
            return PairMoviesInt(source)
        }

        override fun newArray(size: Int): Array<PairMoviesInt<ArrayList<Movie>, Int>?> {
            return arrayOfNulls(size)
        }
    }

}