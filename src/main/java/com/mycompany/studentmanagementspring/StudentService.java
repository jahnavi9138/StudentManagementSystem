

package com.mycompany.studentmanagementspring;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Service
@Transactional
public class StudentService {

    private final SessionFactory sessionFactory;

    public StudentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "FROM Student", Student.class
        ).getResultList();
    }

    @Transactional(readOnly = true)
    public Student getStudentByRollNo(int rollNo) {
        Session session = sessionFactory.getCurrentSession();

        return session.get(Student.class, rollNo);
    }

    public void saveStudent(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(student);
    }

    public void updateStudent(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(student);
    }

    public void deleteStudent(int rollNo) {
        Session session = sessionFactory.getCurrentSession();
        Student student = session.get(Student.class, rollNo);

        if (student != null) {
            session.remove(student);
        }
    }
}
