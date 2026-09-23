package ru.coursework.repository;

import org.hibernate.Session;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Role;
import ru.coursework.model.UserRole;

import java.util.List;

public class UserRoleRepository {

    public List<Role> findRolesByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select r from Role r " +
                                    "join UserRole ur on ur.roleId = r.id " +
                                    "where ur.userId = :userId",
                            Role.class
                    ).setParameter("userId", userId)
                    .getResultList();
        }
    }

    public void save(UserRole userRole) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(userRole);
            session.getTransaction().commit();
        }
    }

    public void delete(UserRole userRole) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(userRole);
            session.getTransaction().commit();
        }
    }
}