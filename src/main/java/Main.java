import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.florianmarsch.preisomat.Server;
import de.florianmarsch.preisomat.jpa.ChargingService;
import de.florianmarsch.preisomat.jpa.CostCentreService;
import de.florianmarsch.preisomat.vo.Cost;
import de.florianmarsch.preisomat.vo.CostCentre;
import de.florianmarsch.preisomat.vo.Person;

public class Main {

	
	public static void main(String[] args) {

		new Main().init();

	}

	private ObjectMapper mapper;
	private CostCentreService costCentreService = new CostCentreService();
	private ChargingService chargingService = new ChargingService();
	
	public void init() {
		
		
		// DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);		
		
		Server server = new Server();
		Integer port = Integer.valueOf(System.getenv("PORT"));
		server.start(port);
		
		server.getBinary("/:view", (request, response) -> {
			return IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("public/index.html")) ;
		});

		
		server.get("/api/costCentres", (request, response) -> {
			
			ArrayList<CostCentre> res = new ArrayList<CostCentre>();
			
			List<CostCentre> allCostCentre = costCentreService.getAllCostCentre();
			for (CostCentre costCentre : allCostCentre) {
				res.add(chargingService.calculate(costCentre));
			}
			return res;
		});
		
		server.post("/api/costCentres", (request, response) -> {
			
			String body = request.body();
			CostCentre costCentre = mapper.readValue(body,CostCentre.class);
			
			costCentreService.createCostCentre(costCentre);
			costCentre = costCentreService.getCostCentre(costCentre.getId());
			return chargingService.calculate(costCentre);
		});
		
		server.get("/api/costCentres/:id", (request, response) -> {
			
			String id = request.params(":id");
			CostCentre costCentre = costCentreService.getCostCentre(id);
			
			return chargingService.calculate(costCentre);
		});
		
		server.post("/api/costCentres/:id/costs", (request, response) -> {
			
			String body = request.body();
			Cost cost = mapper.readerFor(Cost.class).readValue(body);
			
			String id = request.params(":id");
			CostCentre costCentre = costCentreService.getCostCentre(id);
			costCentreService.addCost(costCentre, cost);
			
			return chargingService.calculate(costCentre);
		});
		
		server.post("/api/costCentres/:id/participants", (request, response) -> {
			
			String body = request.body();
			Person person = mapper.readValue(body,Person.class);
			
			String id = request.params(":id");
			CostCentre costCentre = costCentreService.getCostCentre(id);
			costCentreService.addParticipant(costCentre, person);
			
			return chargingService.calculate(costCentre);
		});
		
		
		
	
	}

}
