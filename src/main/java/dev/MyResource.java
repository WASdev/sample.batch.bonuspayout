package dev;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resource")
//@RequestScoped
public class MyResource {

    @Resource(shareable=false, name="jdbc/BonusPayoutDS")
    private DataSource datasource;

    @Inject
    MyBean mybean;

    @GET
    public String getRequest() {

    	System.out.println("DS =" + datasource);

	mybean.m1();

        return "got request";
    }
}

