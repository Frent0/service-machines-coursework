package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.RepairType;

import java.util.List;

public class RepairTypeRepository {

    public List<RepairType> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from RepairType", RepairType.class)
                    .getResultList();
        }
    }

    public void save(RepairType repairType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(repairType);
            session.getTransaction().commit();
        }
    }

    public void delete(RepairType repairType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(repairType);
            session.getTransaction().commit();
        }
    }

    public void update(RepairType repairType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(repairType);
            session.getTransaction().commit();
        }
    }
}