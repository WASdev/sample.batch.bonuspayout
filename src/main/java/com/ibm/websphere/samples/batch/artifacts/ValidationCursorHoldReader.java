/*
 * Copyright 2019 International Business Machines Corp.
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
package com.ibm.websphere.samples.batch.artifacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.websphere.samples.batch.util.BonusPayoutUtils;

@Dependent
public class ValidationCursorHoldReader extends ValidationReader {

    @Inject
    @BatchProperty
    protected String dsJNDI;

    @Inject
    @BatchProperty(name = "startAtIndex")
    protected String startAtIndexStr;
    protected int startAtIndex;

    /*
     * For a partition we want to stop with one partition's worth, not the full set of records.
     */
    @Inject
    @BatchProperty(name = "maxRecordsToValidate")
    protected String maxRecordsToValidateStr;
    protected int maxRecordsToValidate;

    @Inject
    protected JobContext jobCtx;

    @Inject
    protected StepContext stepCtx;

    @Inject
    @BatchProperty
    private String tableName;

    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void setupDBReader() throws NamingException, SQLException {

        ds = BonusPayoutUtils.lookupDataSource(dsJNDI);
        BonusPayoutUtils.validateTableName(tableName);

        startAtIndex = Integer.parseInt(startAtIndexStr);
        maxRecordsToValidate = Integer.parseInt(maxRecordsToValidateStr);

        // For partitions, this should be the partition size, for non-partitioned it will be the full maximum number of records itself.
        int upperBound = startAtIndex + maxRecordsToValidate;

        long instanceID = jobCtx.getInstanceId();

        String query = BonusPayoutUtils.getAccountQuery(tableName);

        logger.fine("Query used in ValidationCursorHoldReader: " + query + "with parms: (" + startAtIndex + "," + upperBound + "," + instanceID + ")");

        conn = ds.getConnection();
        //conn.setAutoCommit(false);
        conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        ps = conn.prepareStatement(query);
        ps.setInt(1, getNextRecordIndex());
        ps.setInt(2, upperBound);
        ps.setLong(3, instanceID);

        rs = ps.executeQuery();
        //conn.commit();

    }

    @Override
    public void close() throws Exception {
        super.close();
        if (rs != null)
            rs.close();
        if (ps != null)
            ps.close();
        if (conn != null)
            conn.close();
    }

    @Override
    protected ResultSet getCurrentResultSet() {
        return rs;
    }

    /*
     * Solves the fact that injection needs to be done on the superclass and subclass independently
     */
    @Override
    protected int getStartAtIndex() {
        return Integer.parseInt(startAtIndexStr);
    }

    @Override
    protected int getMaxRecordsToValidate() {
        return Integer.parseInt(maxRecordsToValidateStr);
    }

    @Override
    protected JobContext getJobContext() {
        return jobCtx;
    }

    @Override
    protected StepContext getStepContext() {
        return stepCtx;
    }
}
