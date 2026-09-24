package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Machine;

import java.util.List;

public class MachineRepository {

    public List<Machine> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select m from Machine m " +
                            "join fetch m.client " +
                            "join fetch m.type",
                    Machine.class
            ).getResultList();
        }
    }

    public List<Machine> findByClientId(Integer clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select m from Machine m " +
                                    "join fetch m.client " +
                                    "join fetch m.type " +
                                    "where m.client.id = :clientId",
                            Machine.class
                    ).setParameter("clientId", clientId)
                    .getResultList();
        }
    }

    public void save(Machine machine) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(machine);
            session.getTransaction().commit();
        }
    }

    public void delete(Machine machine) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(machine);
            session.getTransaction().commit();
        }
    }

    public void update(Machine machine) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(machine);
            session.getTransaction().commit();
        }
    }
}