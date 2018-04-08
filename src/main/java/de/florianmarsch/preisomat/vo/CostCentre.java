package de.florianmarsch.preisomat.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table
@Entity
public class CostCentre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8766493577189328032L;

	@Id
	private String id = UUID.randomUUID().toString();

	@Column
	private String description;

	@Column
	private String name;
	
	@JoinColumn
	@OneToMany(cascade=CascadeType.ALL)
	private List<Person> participants = new ArrayList<Person>();
	
	@JoinColumn
	@OneToMany(cascade=CascadeType.ALL)
	private List<Cost> costs = new ArrayList<Cost>();
	
	@Transient
	private List<Charge> chargings = new ArrayList<Charge>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Person> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Person> participants) {
		this.participants = participants;
	}

	public List<Cost> getCosts() {
		return costs;
	}

	public void setCosts(List<Cost> costs) {
		this.costs = costs;
	}

	public List<Charge> getChargings() {
		return chargings;
	}

	public void setChargings(List<Charge> chargings) {
		this.chargings = chargings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	
}
