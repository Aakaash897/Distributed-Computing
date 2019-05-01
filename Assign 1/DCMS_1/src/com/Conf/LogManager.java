package com.Conf;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	public Handler fileHandler = null;
	public Logger logger;
	
	public LogManager(String serverName) {
        
		logger = Logger.getLogger(serverName);
		try {
			
			fileHandler = new FileHandler(Constants.LOG_DIR+serverName+"\\"+serverName + ".log",true);//creating the server log with server name
			SimpleFormatter formatter = new SimpleFormatter();
	        fileHandler.setFormatter(formatter);
	      //configuring the logger
	        logger.setUseParentHandlers(false);
			logger.addHandler(fileHandler);	
			logger.setLevel(Level.INFO);	
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Exception in logger :: "+e.getMessage());
		}
	}
}
