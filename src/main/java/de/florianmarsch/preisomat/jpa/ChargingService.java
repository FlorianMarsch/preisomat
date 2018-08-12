package de.florianmarsch.preisomat.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.florianmarsch.preisomat.vo.Charge;
import de.florianmarsch.preisomat.vo.Cost;
import de.florianmarsch.preisomat.vo.CostCentre;
import de.florianmarsch.preisomat.vo.Person;

public class ChargingService {

	public CostCentre calculate(CostCentre aCostCentre) {
		
			
			
			List<Cost> allCosts = aCostCentre.getCosts();
			List<Cost> fixCosts = new ArrayList<Cost>();
			List<Cost> variableCosts = new ArrayList<Cost>();
			

			List<Person> persons = aCostCentre.getParticipants();
			Map<Person, Charge> charges = getEmptyCharging(persons);
			
			for (Cost cost : allCosts) {
				
				Person payer = cost.getPayer();
				Charge charge= charges.get(payer);
				charge.setCharge(charge.getCharge().add(cost.getPrice()));
				charge.setSaldo(charge.getSaldo().add(cost.getPrice()));
				
				
				cost.setShare(new HashMap<>());
				
				if(cost.getFixcost()) {
					fixCosts.add(cost);
				}else {
					variableCosts.add(cost);
				}
			}
			

			
			
			
			for (Cost cost : fixCosts) {
				
				List<Person> participants = persons;
				Integer days = 0;
				for (Person person : participants) {
					days = days + person.getDays();
				}
				
				BigDecimal perDay = cost.getPrice().divide(new BigDecimal(days), 4 , RoundingMode.HALF_EVEN);
				for (Person person : participants) {
					Charge charge = charges.get(person);
					BigDecimal share = new BigDecimal(person.getDays());
					BigDecimal costPrice = share.multiply(perDay);
					charge.setSaldo(charge.getSaldo().subtract(costPrice));
					
					cost.getShare().put(person.getName(),costPrice);
				}
			}
			
			for (Cost cost : variableCosts) {
				
				List<Person> participants = cost.getParticipants();
				
				
				BigDecimal perParticipant = null;
				if(participants.isEmpty()) {
					perParticipant = BigDecimal.ZERO;
				}else {
					perParticipant = cost.getPrice().divide(new BigDecimal(participants.size()), 4, RoundingMode.HALF_EVEN);
				}
				
				for (Person person : participants) {
					Charge charge = charges.get(person);
					BigDecimal costPrice = perParticipant;
					charge.setSaldo(charge.getSaldo().subtract(costPrice));
					cost.getShare().put(person.getName(),costPrice);
				}
			}


			aCostCentre.setChargings(new ArrayList<>(charges.values()));

			
		
		
		
		return aCostCentre;
	}

	Map<Person, Charge> getEmptyCharging(List<Person> persons) {
		Map<Person, Charge> charges = new HashMap<>();
		
		
		for (Person person : persons) {
			Charge charge = new Charge();
			charge.setPerson(person);
			charge.setCharge(new BigDecimal("0"));
			charge.setSaldo(new BigDecimal("0"));
			charges.put(person, charge);
		}
		return charges;
	}
	
}
