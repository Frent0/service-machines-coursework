package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Role;

import java.util.List;

public class RoleRepository {

    public List<Role> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Role", Role.class).getResultList();
        }
    }

    public void save(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(role);
            session.getTransaction().commit();
        }
    }

    public void delete(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(role);
            session.getTransaction().commit();
        }
    }

    public void update(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(role);
            session.getTransaction().commit();
        }
    }

    public Role findByName(String name) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "from Role where name = :name",
                            Role.class
                    ).setParameter("name", name)
                    .uniqueResult();
        }
    }
}