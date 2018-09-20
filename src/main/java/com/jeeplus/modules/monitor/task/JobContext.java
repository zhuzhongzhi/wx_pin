package com.jeeplus.modules.monitor.task;

import javax.servlet.ServletContext;

public class JobContext {
	 private final static JobContext instance = new JobContext();  
	 private static ServletContext context;  
	   
	 private JobContext()  
	 {  
	 }  
	  
	 public static JobContext getInstance()  
	 {  
		 return instance;  
	 }  
	   
	 public void setContext(ServletContext context2)  
	 {  
		 context = context2;  
	 }  
	   
	 public ServletContext getContext()  
	 {  
		 return context;  
	 }  
}
