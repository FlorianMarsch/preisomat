package de.florianmarsch.preisomat.vo;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table
@Entity
public class Person {

	@Id
	private String id = UUID.randomUUID().toString();

	@Column
	private String name;

	@Column
	private Integer days;



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Integer getDays() {
		return days;
	}



	public void setDays(Integer days) {
		this.days = days;
	}



	public JSONObject getJSONObject() {
		JSONObject JSONObject = new JSONObject();
		try {
			JSONObject.put("id", getId());
			JSONObject.put("name", getName());
			JSONObject.put("days", getDays());
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return JSONObject;
	}
}
