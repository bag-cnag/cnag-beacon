package org.cnag.rdconnect.beacon.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cnag.rdconnect.beacon.model.Chromosome;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.json.JSONObject;
import org.json.JSONArray;


// import org.elasticsearch.action.search.SearchResponse;
// import org.elasticsearch.client.Client;
// import org.elasticsearch.client.transport.PreBuiltTransportClient;
// import org.elasticsearch.common.transport.InetSocketTransportAddress;
// import org.elasticsearch.index.query.QueryBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
// import org.elasticsearch.search.SearchHit;

/**
 * CNAG implementation of a RD-Connect beacon service.
 * ElasticSearch DAO to query into rdconnect dataset
 * @author Joan Protasio (joan.protasio@cnag.crg.eu)
 * @version 1.0
 */

public class DAOElasticSearch {	
	
	private static Log log = LogFactory.getLog(DAOElasticSearch.class);
	static Properties prop = new Properties();
	static InputStream input = null;
	static String elasticSearchInstance;
	static String elasticSearchIndex;
	static String elasticSearchType;
	static String username;
	static String password;
	static int elasticSearchPort;
	
	
	public static void loadConf() {
		
		// loading properties file
		try {
    		input = DAOElasticSearch.class.getClassLoader().getResourceAsStream("beacon.properties");    		
			prop.load(input);
			elasticSearchInstance = prop.getProperty("elasticsearch.server");
			elasticSearchIndex    = prop.getProperty("elasticsearch.index");
			elasticSearchType     = prop.getProperty("elasticsearch.type");
			elasticSearchPort	  = Integer.parseInt(prop.getProperty("elasticsearch.port"));
			username     = prop.getProperty("username");
			password     = prop.getProperty("password");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	
	public boolean searchVariant(String chrom, Long pos, String allele, String ref, String dataset) throws UnknownHostException {		
		
		boolean foundVariant = false;
		
		loadConf();
		log.info("# es instance: " + elasticSearchInstance);
		log.info("# es port: " + elasticSearchPort);		
		
		// for internal details of elasticsearch schema
		Chromosome chromosome = Chromosome.fromString(chrom);
		
		switch(chromosome){			
			case CHRMT:
				chrom = Integer.toString(23);
				break;
			case CHRX:
				chrom = Integer.toString(24);
				break;
			case CHRY:
				chrom = Integer.toString(25);
				break;
			default:
				break;			
		}
				
		@SuppressWarnings("resource")
		/*Settings settings = Settings.builder()
        .put("cluster.name", "cluster642231a").build();
		//Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(elasticSearchInstance, elasticSearchPort));                
		TransportClient client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchInstance), elasticSearchPort));		
		QueryBuilder filter = QueryBuilders.andQuery(QueryBuilders.termQuery("chrom", chrom), QueryBuilders.termQuery("pos", pos));	

		//FilterBuilder filter = FilterBuilders.andFilter(FilterBuilders.termFilter("chrom", chrom), FilterBuilders.termFilter("pos", pos));		
		SearchResponse response = client.prepareSearch(elasticSearchIndex).setTypes(elasticSearchType).setPostFilter(filter).execute().actionGet();*/
int i;
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(username,password));
		WebResource webResource = client
		   .resource("http://"+elasticSearchInstance+":"+elasticSearchPort+"/"+elasticSearchIndex+"/_search?q=chrom%3A"+chrom+"%20AND%20pos%3A"+pos);

		ClientResponse response = webResource.accept("application/json")
                   .get(ClientResponse.class);

		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
         
		String output = response.getEntity(String.class);
        final JSONObject obj = new JSONObject(output);
         final JSONObject hits = obj.getJSONObject("hits");

         final JSONArray hits_array  = hits.getJSONArray("hits");
         //final JSONObject hits_array_0 = hits_array.getJSONObject(0);
         
         //final JSONObject source =  hits_array.get(0);
         //System.out.println(hits_array_0);
		
		
        final int n = hits_array.length();
		 for (int item = 0; item < n; ++item){
  final JSONObject hits_array_0 = hits_array.getJSONObject(item);
final JSONObject source =hits_array_0.getJSONObject("_source");
if (allele.equalsIgnoreCase(source.getString("alt")))
		return true;						   
		//   }

		}


return foundVariant;


			
		// for (  SearchHit hit : response.getHits()) {
		// 	Map<String,Object> element = hit.getSource();
			
		// 	// for testing purpose
		// 	//log.info("es response: " + response.toString());
			
		// 	if (allele.equalsIgnoreCase(element.get("alt").toString()))
		// 		return true;						   
		//   }
		
		// client.close();		
		
	 
}

}