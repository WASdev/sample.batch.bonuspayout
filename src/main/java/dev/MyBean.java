package dev;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resource")
@RequestScoped
public class MyBean {

    @Resource(shareable=false, name="jdbc/BonusPayoutDS")
    private DataSource datasource;

    public void m1() {

    	System.out.println("In MyBean, DS =" + datasource);
    }
}

