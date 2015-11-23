package edu.gatech.cc.lostandfound.api;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.gatech.cc.lostandfound.api.model.LostReport;

/**
 * Created by mkatri on 11/22/15.
 */
public class OfyHelper implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ObjectifyService.register(LostReport.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
