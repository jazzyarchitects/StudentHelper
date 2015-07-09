package com.jazzyarchitects.studentassistant.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jibin_ism on 09-Jul-15.
 */
public class Subject implements Parcelable {

    public Subject(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static Parcelable.Creator<Subject> CREATOR=new Parcelable.ClassLoaderCreator<Subject>(){
        @Override
        public Subject createFromParcel(Parcel source, ClassLoader loader) {
            return new Subject(source);
        }

        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
