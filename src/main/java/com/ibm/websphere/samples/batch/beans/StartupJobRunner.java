/*
 * Copyright 2020 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.batch.beans;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.websphere.samples.batch.util.BonusPayoutUtils;

@Singleton
@RunAs("JOBSTARTER")
@Startup
//@ApplicationScoped
@TransactionManagement(TransactionManagementType.BEAN)
public class StartupJobRunner {

	@Resource(shareable=false, name="jdbc/BonusPayoutDS")
	private DataSource datasource;

    	
    //@Schedule(hour = "*", minute = "*", second = "*/20", persistent = false)
    public void runJob() {
        System.out.println("\n\nRunning batch job from the StartupJobRunner bean\n\n");
    	System.out.println("from runner DS =" + datasource);

        try {
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            Properties parms = new Properties();
            parms.setProperty("AAA", "111");
            jobOperator.start("BonusPayoutJob", parms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    static final long INITIAL_DELAY = 10;
    static final long PERIOD = 15;

    
    @Resource
    ManagedScheduledExecutorService scheduler;
    
    @PostConstruct
    public void init() {
    	
    	try {
    	Object obj = new InitialContext().lookup("java:comp/UserTransaction");
    	System.out.println("SKSK: jndi init(): " + obj);
    	} catch (Exception e) { 
    		   System.out.println("SKSK: caught exception and printing stack trace for " + e );
    		     e.printStackTrace(); 
    		     throw new RuntimeException(e); 
    	}
    	
        this.scheduler.scheduleWithFixedDelay(this::runJob, 
                INITIAL_DELAY, PERIOD, 
                TimeUnit.SECONDS);
    }
}

