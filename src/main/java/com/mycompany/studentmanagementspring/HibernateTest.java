
package com.mycompany.studentmanagementspring;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateTest {

    public static void main(String[] args) {

        SessionFactory factory = null;

        try {
            factory = new org.hibernate.cfg.Configuration()
                    .configure()
                    .addAnnotatedClass(Student.class)
                    .buildSessionFactory();

            Session session = factory.openSession();

            System.out.println("Hibernate connected successfully!");

            session.close();

        } catch (Exception e) {
            System.out.println("Hibernate connection failed!");
            e.printStackTrace();

        } finally {
            if (factory != null) {
                factory.close();
            }
        }
    }
}
