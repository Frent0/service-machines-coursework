package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.MachineType;

import java.util.List;

public class MachineTypeRepository {

    public List<MachineType> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from MachineType", MachineType.class)
                    .getResultList();
        }
    }

    public void save(MachineType machineType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(machineType);
            session.getTransaction().commit();
        }
    }

    public void delete(MachineType machineType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(machineType);
            session.getTransaction().commit();
        }
    }

    public void update(MachineType machineType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(machineType);
            session.getTransaction().commit();
        }
    }
}