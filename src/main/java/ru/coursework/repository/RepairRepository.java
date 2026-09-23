package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Repair;

import java.util.List;

public class RepairRepository {

    public List<Repair> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery(
                            "select r from Repair r " +
                                    "join fetch r.machine " +
                                    "join fetch r.repairType",
                            Repair.class
                    )
                    .getResultList();
        }
    }

    public void save(Repair repair) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(repair);
            session.getTransaction().commit();
        }
    }

    public void delete(Repair repair) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(repair);
            session.getTransaction().commit();
        }
    }

    public void update(Repair repair) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(repair);
            session.getTransaction().commit();
        }
    }
}