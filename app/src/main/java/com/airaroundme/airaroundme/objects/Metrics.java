package com.airaroundme.airaroundme.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Metrics implements Parcelable {
	
	private String name;
	private String avg;
	private String avgDesc;
	private String min;
	private String max;
	private Data data;
	protected Metrics(Parcel in) {
		name = in.readString();
		avg = in.readString();
		avgDesc = in.readString();
		min = in.readString();
		max = in.readString();
		data = (Data) in.readValue(Data.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(avg);
		dest.writeString(avgDesc);
		dest.writeString(min);
		dest.writeString(max);
		dest.writeValue(data);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Metrics> CREATOR = new Parcelable.Creator<Metrics>() {
		@Override
		public Metrics createFromParcel(Parcel in) {
			return new Metrics(in);
		}

		@Override
		public Metrics[] newArray(int size) {
			return new Metrics[size];
		}
	};

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
	public String getAvgDesc() {
		return avgDesc;
	}
	public void setAvgDesc(String avgDesc) {
		this.avgDesc = avgDesc;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}


}
