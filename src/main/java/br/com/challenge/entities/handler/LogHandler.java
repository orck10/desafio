package br.com.challenge.entities.handler;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;

@Document
public class LogHandler {
	
	@Id
	private String id;
	private String method;
	private String endPoint;
	private String data;
	private LocalDateTime date;
	
	public LogHandler(String method, String endPoint, String data, LocalDateTime date) {
		this.setMethod(method);
		this.setEndPoint(endPoint);
		this.data = data;
		this.date = date;
	}
	
	public String getId() {
		return id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
