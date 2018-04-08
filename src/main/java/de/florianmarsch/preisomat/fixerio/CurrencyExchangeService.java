package de.florianmarsch.preisomat.fixerio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyExchangeService {

	public static BigDecimal cached = null;
	
	public BigDecimal getSEK() {
		if(cached != null) {
			return cached;
		}
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://api.fixer.io/latest?base=SEK&symbols=EUR");
			request.setHeader("Content-Type", "application/json");
			
			HttpResponse response = client.execute(request);
			
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				
				String json = result.toString();
				
				String rate = new JSONObject(json).getJSONObject("rates").getString("EUR");

				cached= new BigDecimal(rate);
				return cached;
		} catch (IllegalStateException | IOException | JSONException e) {
			e.printStackTrace();
			return new BigDecimal("0.1");
		}
	}
	
}
