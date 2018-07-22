package de.florianmarsch.preisomat.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Table
@Entity
public class Cost implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1083092677531608672L;


	@Id
	private String id = UUID.randomUUID().toString();

	@JoinColumn
	private Person payer;

	@Column
	private String description;
	
	@Column
	private Boolean fixcost = Boolean.TRUE;
	
	@JoinColumn
	@ManyToMany
	private List<Person> participants;
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private Map<String, BigDecimal> share;

	@Column
	private String nativeCurrency;
	
	@Column(precision=8, scale=2) 
	private BigDecimal price;

	@Column(precision=8, scale=2) 
	private BigDecimal priceSEK;
	
	@Column
	private BigDecimal sekExchange;

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

	public Boolean getFixcost() {
		return fixcost;
	}

	public void setFixcost(Boolean fixcost) {
		this.fixcost = fixcost;
	}

	public List<Person> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Person> participants) {
		this.participants = participants;
	}

	public Map<String, BigDecimal> getShare() {
		return share;
	}

	public void setShare(Map<String, BigDecimal> share) {
		this.share = share;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceSEK() {
		return priceSEK;
	}

	public void setPriceSEK(BigDecimal priceSEK) {
		this.priceSEK = priceSEK;
	}

	public BigDecimal getSekExchange() {
		return sekExchange;
	}

	public void setSekExchange(BigDecimal sekExchange) {
		this.sekExchange = sekExchange;
	}

	public Person getPayer() {
		return payer;
	}

	public void setPayer(Person payer) {
		this.payer = payer;
	}

	public String getNativeCurrency() {
		return nativeCurrency;
	}

	public void setNativeCurrency(String nativeCurrency) {
		this.nativeCurrency = nativeCurrency;
	}

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
		Cost other = (Cost) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
