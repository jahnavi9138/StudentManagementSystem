
package com.mycompany.studentmanagementspring;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebListener
public class SpringContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfig.class);

        event.getServletContext().setAttribute("springContext", context);

        System.out.println("Spring started successfully!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Object context = event.getServletContext().getAttribute("springContext");

        if (context instanceof AnnotationConfigApplicationContext) {
            ((AnnotationConfigApplicationContext) context).close();
        }
    }
}
