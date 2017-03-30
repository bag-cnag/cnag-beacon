/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package org.cnag.rdconnect.beacon.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cnag.rdconnect.beacon.model.Beacon;
import org.cnag.rdconnect.beacon.model.BeaconResponse;
import org.cnag.rdconnect.beacon.model.Chromosome;
import org.cnag.rdconnect.beacon.model.Dataset;
import org.cnag.rdconnect.beacon.model.Error;
import org.cnag.rdconnect.beacon.model.Query;
import org.cnag.rdconnect.beacon.model.Reference;
import org.cnag.rdconnect.beacon.model.Response;
import org.cnag.rdconnect.beacon.util.DAOElasticSearch;
import org.cnag.rdconnect.beacon.util.QueryUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * CNAG implementation of a RD-Connect beacon service.
 * 
 * @author Joan Protasio (joan.protasio@cnag.crg.eu)
 * @version 1.0
 */

public class SampleBeaconService implements BeaconService {
	
	private Dataset dataset;
    private List<Dataset> datasets;

    private Query query;
    private List<Query> queries;

    private Beacon beacon;
    
    private static Log log = LogFactory.getLog(SampleBeaconService.class);
    private DAOElasticSearch daoES = new DAOElasticSearch();
    
    public SampleBeaconService() {    	
    	this.dataset = new Dataset("rdconnect", "RD-Connect Variants Beacon", "HG19", null, null);
        this.query = new Query("T", Chromosome.CHR1, 65720708L, Reference.HG19, "rdconnect");

        this.queries = new ArrayList<>();
        this.queries.add(query);
        this.datasets = new ArrayList<>();
        this.datasets.add(dataset);

        this.beacon = new Beacon("rdconnect", "RD-Connect", "CNAG", "RD-Connect Variants Beacon", "1.0", "https://platform.rd-connect.eu", "platform@rd-connect.eu", "CAS", datasets, queries);
	}

	@Override
	public BeaconResponse query(String chrom, Long pos, String allele, String ref, String dataset) {
		// check if required parameters are missing
        if (chrom == null || pos == null || allele == null || ref == null) {
            Error errorResource = new Error("incomplete query", "missing required params");
            Response responseResource = new Response(null, null, null, null, errorResource);
            BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), responseResource);
            return response;
        }  
                
        // query to manager DAOElasticSearch
        boolean foundVariant = daoES.searchVariant(chrom, pos, allele, ref, dataset);   
        //log.info("implementation beacon interface, result query: " + foundVariant);
               
        BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), null);
        Response responseResource = new Response(foundVariant, 0, null, "variant", null);        
        response.setResponse(responseResource);        
        
        return response;
	}

	@Override
	public Beacon info() {
		return beacon;
	}
}