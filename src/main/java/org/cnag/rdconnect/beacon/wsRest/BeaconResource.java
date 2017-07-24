/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cnag.rdconnect.beacon.wsRest;

import org.cnag.rdconnect.beacon.model.BeaconResponse;
import org.cnag.rdconnect.beacon.model.Response;
import org.cnag.rdconnect.beacon.service.BeaconService;
import org.cnag.rdconnect.beacon.service.SampleBeaconService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * Beacon rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/query")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class BeaconResource {

    private BeaconService service = new SampleBeaconService();

    @GET
    public BeaconResponse query(@QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele, @QueryParam("ref") String ref, @QueryParam("dataset") String dataset) {

    return service.query(chrom, pos, allele, ref, dataset);

    }
    
    @POST
   	@Path("/match")
   	public String createProductInJSON(@Context HttpHeaders headers, String a) throws IOException {

   		System.out.println("=> Wrapper to MME call!");   		
		//URL url = new URL("http://localhost:9000/api/v1/match/rdconnect");
   		URL url = new URL("http://rdcompute1.rd-connect.eu:8016/api/v1/match/rdconnect");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("X-AUTH-TOKEN", headers.getRequestHeaders().getFirst("X-AUTH-TOKEN"));	
		
	
		OutputStream os = conn.getOutputStream();
		os.write(a.getBytes());
		os.flush();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		String jsonraw = "";
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			jsonraw=jsonraw+output;
		}

		conn.disconnect();
   		
   		return jsonraw;

   	}
    
    
    
}
