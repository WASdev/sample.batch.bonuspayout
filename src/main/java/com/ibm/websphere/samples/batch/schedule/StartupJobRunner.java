package com.ibm.websphere.samples.batch.schedule;

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
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.sql.DataSource;

@Singleton
@RunAs("JOBSTARTER")
@Startup
public class StartupJobRunner {

    public void runJob() {
        try {
        	System.out.println("SKSK: Running scheduled job");

            JobOperator jobOperator = BatchRuntime.getJobOperator();
            Properties parms = new Properties();
            parms.setProperty("started-by", this.getClass().getCanonicalName());
            jobOperator.start("BonusPayoutJob", parms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static final long INITIAL_DELAY = 6;
    static final long PERIOD = 15;
    
    @Resource
    ManagedScheduledExecutorService scheduler;
    
    @PostConstruct
    public void init() {
    	if (scheduler != null) {
    		this.scheduler.scheduleWithFixedDelay(this::runJob, 
                INITIAL_DELAY, PERIOD, 
                TimeUnit.SECONDS);
    	}
    }
}



