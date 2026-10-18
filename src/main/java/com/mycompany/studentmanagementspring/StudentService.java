
package com.mycompany.studentmanagementspring;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final SessionFactory sessionFactory;

    public StudentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Admin and staff: view all students
    public List<Student> getAllStudents() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Student", Student.class
            ).getResultList();
        }
    }

    // Student: view only their own record
    public List<Student> getStudentByRollNo(int rollNo) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Student WHERE rollNo = :rollNo",
                Student.class
            )
            .setParameter("rollNo", rollNo)
            .getResultList();
        }
    }
}
