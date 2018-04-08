package de.florianmarsch.preisomat.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Table
@Entity
public class Charge implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6897356605770737118L;

	@Id
	private String id = UUID.randomUUID().toString();
	
	@JoinColumn
	private Person person;
	
	@Column
	private BigDecimal charge;
	
	@Column
	private BigDecimal saldo;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	
}
