package com.samanecorporation.security.config;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.samanecorporation.security.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
	private HibernateUtil() {
		
	}
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				PropertiesReader reader = new PropertiesReader("database.properties"); 
								
				Configuration configuration = new Configuration();

 				Properties settings = new Properties();
				settings.put(AvailableSettings.DRIVER, "com.mysql.cj.jdbc.Driver");

				LOG.info("URL DB : {} ", reader.getProperty("db.urlProd"));
				LOG.info("DB USER: {}", reader.getProperty("db.user"));
				LOG.info("DB PASSWORD : {}", reader.getProperty("db.pwd"));

				Map<String, String> env = System.getenv();
				String dbUrlProdurl = env.get("DB_URL_PROD");
				String securityDbUser = env.get("SECURITY_DB_USER");
				String securityDbPwd = env.get("SECURITY_DB_PWD");

				LOG.info("URL DB POUR DOCKER : {}", dbUrlProdurl);
				LOG.info("DB USER POUR DOCKER : {}", securityDbUser);
				LOG.info("DB PASSWORD POUR DOCKER : {}", securityDbPwd);


				if (!dbUrlProdurl.isBlank() && !securityDbUser.isBlank() && !securityDbPwd.isBlank()) {
					settings.put(AvailableSettings.URL, dbUrlProdurl);
					settings.put(AvailableSettings.USER, securityDbUser);
					settings.put(AvailableSettings.PASS, securityDbPwd);
				} else {
					settings.put(AvailableSettings.URL, reader.getProperty("db.urlDev"));
					settings.put(AvailableSettings.USER, reader.getProperty("db.username"));
					settings.put(AvailableSettings.PASS, reader.getProperty("db.password"));
				}
				settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
				//cette ligne est très importante
				settings.put(AvailableSettings.HBM2DDL_AUTO, "update");
				
				settings.put(AvailableSettings.SHOW_SQL, "true");
				settings.put(AvailableSettings.FORMAT_SQL, "true");

				settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(UserEntity.class);
 
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
 				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				
 				return sessionFactory;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}