package de.florianmarsch.preisomat.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.florianmarsch.preisomat.fixerio.CurrencyExchangeService;
import de.florianmarsch.preisomat.vo.Cost;
import de.florianmarsch.preisomat.vo.CostCentre;
import de.florianmarsch.preisomat.vo.Person;

public class CostCentreService {
	
	private CurrencyExchangeService ces = new CurrencyExchangeService();

	public List<CostCentre> getAllCostCentre(){
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		TypedQuery<CostCentre> query = em.createQuery("Select x from CostCentre x", CostCentre.class);
		List<CostCentre> resultList = query.getResultList();
		
		em.getTransaction().commit();
		return new ArrayList<CostCentre>(resultList);
	}
	
	public CostCentre getCostCentre(String aId){
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		CostCentre find = em.find(CostCentre.class, aId);
		
		em.getTransaction().commit();
		return find;
	}
	
	public void createCostCentre(CostCentre aCostCentre){
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		
		if(aCostCentre.getId() != null) {
			
			CostCentre original = em.find(CostCentre.class, aCostCentre.getId());
			original.setName(aCostCentre.getName());
			original.setDescription(aCostCentre.getDescription());
			
			original.setParticipants(aCostCentre.getParticipants());
			
			em.merge(original);
			
		}else {
			em.persist(aCostCentre);
		}
		
		
		
		em.getTransaction().commit();
	}
	
	public void addParticipant(CostCentre aCostCentre, Person participant){
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		
		aCostCentre.getParticipants().add(participant);
		em.merge(aCostCentre);
		
		
		em.getTransaction().commit();
	}
	
	public void addCost(CostCentre aCostCentre, Cost aCost){
		
		EntityManager em = new EmFactory().produceEntityManager();
		em.getTransaction().begin();
		
		
		BigDecimal priceSEK = aCost.getPrice();
		if(! aCost.getNativeCurrency().equals("EUR")){
			
			BigDecimal sekExchange = ces.getSEK();
			BigDecimal priceEUR = priceSEK.multiply(sekExchange);
			aCost.setPrice(priceEUR);
			aCost.setPriceSEK(priceSEK);
			aCost.setSekExchange(sekExchange);
			aCost.setNativeCurrency("EUR");
		}
		
		Iterator<Cost> iterator = aCostCentre.getCosts().iterator();
		while (iterator.hasNext()) {
			Cost cost = (Cost) iterator.next();
			if(cost.equals(aCost)) {
				iterator.remove();
			}
		}
		
		aCostCentre.getCosts().add(aCost);
		em.merge(aCostCentre);
		
		
		em.getTransaction().commit();
	}
	

	
}
