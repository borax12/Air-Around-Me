package com.airaroundme.airaroundme.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable{

	private String date;
	private String value;
	private String tooltip;
	private String color;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


	protected Data(Parcel in) {
		date = in.readString();
		value = in.readString();
		tooltip = in.readString();
		color = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(value);
		dest.writeString(tooltip);
		dest.writeString(color);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
	
	

}
