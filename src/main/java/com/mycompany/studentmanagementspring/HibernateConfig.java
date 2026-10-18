package com.mycompany.studentmanagementspring;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                String dbPassword = System.getenv("DB_PASSWORD");

                Properties settings = new Properties();
                settings.put(Environment.URL, "jdbc:mysql://mysql-d360566-student-management-system-9138.i.aivencloud.com:28261/defaultdb?sslMode=REQUIRED&enabledTLSProtocols=TLSv1.2,TLSv1.3&allowPublicKeyRetrieval=true");
                settings.put(Environment.USER, "avnadmin");
                settings.put(Environment.PASS, dbPassword != null ? dbPassword : "");
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Student.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}