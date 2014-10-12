package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.models.Coach;

public class Headers {
	private String name;
	private ArrayList<Coach> coachesList = new ArrayList<Coach>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Coach> getCoachNameList() {
		return coachesList;
	}
	
	public void setCoachesNameList(ArrayList<Coach> coachesList) {
		this.coachesList = coachesList;
	}
}
