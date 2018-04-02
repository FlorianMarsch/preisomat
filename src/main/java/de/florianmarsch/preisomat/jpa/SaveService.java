package de.florianmarsch.preisomat.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import de.florianmarsch.preisomat.fixerio.CurrencyExchangeService;
import de.florianmarsch.preisomat.vo.Cost;
import de.florianmarsch.preisomat.vo.Person;
import de.florianmarsch.preisomat.vo.SyncPoint;

public class SaveService {

	private CurrencyExchangeService ces = new CurrencyExchangeService();
	private DataService dataService = new DataService();
	
	public void saveSyncPoint(SyncPoint aSyncPoint){
		Cost cost = new Cost();
		cost.setDescription(aSyncPoint.getDescription());
		cost.setPerson(aSyncPoint.getName());
		String currency = aSyncPoint.getCurrency();
		BigDecimal price = new BigDecimal(aSyncPoint.getPrice());
		if(currency.equals("EUR")){
			cost.setPrice(price);
			cost.setPriceSEK(null);
		}else{
			cost.setPrice(price.multiply(ces.getSEK()));
			cost.setPriceSEK(price);
		}
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		em.persist(cost);
		em.getTransaction().commit();
	}
	
	
	public void savePerson(Person aPerson){
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		
		Person find = null;
		
		List<Person> persons = dataService.getPersons();
		for (Person person : persons) {
			if(person.getName().equalsIgnoreCase(aPerson.getName())) {
				find = person;
			}
		}
		
		if(find != null){
			find.setDays(aPerson.getDays());
			em.merge(find);
		}else {
			aPerson.setId(UUID.randomUUID().toString());
			em.persist(aPerson);
		}
		
		
		em.getTransaction().commit();
	}
}
