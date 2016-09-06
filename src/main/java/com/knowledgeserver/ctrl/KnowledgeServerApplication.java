package com.knowledgeserver.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.knowledgeserver.database.CacheDAO;
import com.knowledgeserver.database.HyperSqlDbServer;
import com.knowledgeserver.database.QuerySubscriptionDAO;
import com.knowledgeserver.model.SystemParameters;

@SpringBootApplication
@EnableScheduling
public class KnowledgeServerApplication {
	
	public static SystemParameters sp;
	
	public static void main(String[] args) throws FileNotFoundException {
		ApplicationContext ctx =  SpringApplication.run(KnowledgeServerApplication.class, args); 
		setSystemParameters("SystemParameters.txt");
		
		GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
				  .globalJmxStatistics()
				  .allowDuplicateDomains(true)
				  .build();

		DefaultCacheManager dcm = new DefaultCacheManager(globalConfig);
		
		// start DB server
		new HyperSqlDbServer(sp);
		
		// Create connections to databases and cache
		QuerySubscriptionDAO querySubscriptionDAO = new QuerySubscriptionDAO(sp);
		SparqlCtrl sparqlCtrl = new SparqlCtrl(sp);
		CacheDAO cacheDAO = new CacheDAO(sp, dcm);
		
		// rest interface for HTTP requests and responses
        RestCtrl restCtrl = ctx.getBean(RestCtrl.class);
        restCtrl.setSp(sp);
        restCtrl.setQuerySubscriptionDAO(querySubscriptionDAO);
        restCtrl.setSparqlCtrl(sparqlCtrl);  
        restCtrl.setQueryCacheDAO(cacheDAO);
        
    	// scheduled subscription tasks !!
		QueryCtrl queryCtrl = ctx.getBean(QueryCtrl.class);
		queryCtrl.setSp(sp);
		queryCtrl.setQuerySubscriptionDAO(querySubscriptionDAO);
		queryCtrl.setSparqlCtrl(sparqlCtrl);
		queryCtrl.setQueryCacheDAO(cacheDAO);
		
        

		// scheduled subscription tasks !!
		SubscriptionCtrl subsCtrl = ctx.getBean(SubscriptionCtrl.class);
		subsCtrl.setSp(sp);
		subsCtrl.setQuerySubscriptionDAO(querySubscriptionDAO);
		subsCtrl.setSparqlCtrl(sparqlCtrl);
		subsCtrl.setInitialSubscriptionCache(dcm);
		
        
	}

	public static void setSystemParameters(String s) throws FileNotFoundException{
        // set default values
		String uname="sa", password="", hsqldb_port="9001", sparql_port="3030",  sparql_URL="http://localhost",dbname="mainDB", 
				dir="file:\\C:\\SemantixEngine\\SQL_database\\mainDB";
        int maxCacheEntries=1000;
		double subscribeTimeInterval=10;
        
        Scanner sc = new Scanner(new File(s));
    	while (sc.hasNext()) {
    		String tmp = sc.next();
    		if(tmp.equalsIgnoreCase("hsqldb_port")){
    			hsqldb_port = sc.next();
    		}
    		else if(tmp.equalsIgnoreCase("sparql_port")){
    			sparql_port = sc.next();
    		}		
    		else if(tmp.equalsIgnoreCase("sparql_URL")){
    			sparql_URL = sc.next();
    		}
    		else if(tmp.equalsIgnoreCase("hsqldb_username")){
    			uname = sc.next();
    		}
    		else if(tmp.equalsIgnoreCase("hsqldb_password")){
    			password = ""; 
    		}
    		else if(tmp.equalsIgnoreCase("hsqldb_dbname")){
    			dbname = sc.next();
    		}
    		else if(tmp.equalsIgnoreCase("hsqldb_directory")){
    			dir = sc.next();
    		}
    		else if(tmp.equalsIgnoreCase("subscribe_time_interval_seconds")){
    			subscribeTimeInterval = Double.parseDouble(sc.next());
    		}
    		else if(tmp.equalsIgnoreCase("max_cache_entries")){
    			maxCacheEntries = Integer.parseInt(sc.next());
    		}
    	}
    
    	sp = new SystemParameters(hsqldb_port, sparql_port,sparql_URL, uname, password,dbname,dir,subscribeTimeInterval, maxCacheEntries);
    	sc.close();
    }
}
