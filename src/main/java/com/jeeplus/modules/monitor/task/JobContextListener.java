package com.jeeplus.modules.monitor.task;

import javax.servlet.ServletContextEvent;

public class JobContextListener {
	 public void contextDestroyed(ServletContextEvent servletcontextevent)  
	 {  
		  // TODO Auto-generated method stub  
		    
	 }  
	  
	 public void contextInitialized(ServletContextEvent servletcontextevent)  
	 {  
		  // TODO Auto-generated method stub  
		  JobContext.getInstance().setContext(servletcontextevent.getServletContext());  
	 }  
}
