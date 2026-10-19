
package com.mycompany.studentmanagementspring;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(
                    HibernateConfig.class
            );

            System.out.println(
                    "Spring started successfully!"
            );

            System.out.println(
                    "StudentService bean exists: "
                    + context.containsBean("studentService")
            );

            event.getServletContext().setAttribute(
                    "springContext", context
            );

        } catch (Exception e) {
            System.err.println(
                    "Spring failed to start!"
            );
            e.printStackTrace();

            if (context != null) {
                context.close();
            }

            throw new RuntimeException(
                    "Failed to initialize Spring context.", e
            );
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Object springObject = event.getServletContext()
                .getAttribute("springContext");

        if (springObject instanceof
                AnnotationConfigApplicationContext) {
            ((AnnotationConfigApplicationContext)
                    springObject).close();

            System.out.println(
                    "Spring context closed."
            );
        }
    }
}
