package com.airaroundme.airaroundme.objects;

public class FetchedData {
	
	private String title;
	private String down;
	private String downmessage;
	private String date;
	private Aqi aqi;
	private Metrics metrics[];
	
	
	
	public Metrics[] getMetrics() {
		return metrics;
	}
	public void setMetrics(Metrics[] metrics) {
		this.metrics = metrics;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDown() {
		return down;
	}
	public void setDown(String down) {
		this.down = down;
	}
	public String getDownmessage() {
		return downmessage;
	}
	public void setDownmessage(String downmessage) {
		this.downmessage = downmessage;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Aqi getAqi() {
		return aqi;
	}
	public void setAqi(Aqi aqi) {
		this.aqi = aqi;
	}
	
	
	

}
