package ru.coursework.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.coursework.model.*;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration c = new Configuration().configure("hibernate.cfg.xml");
            c.addAnnotatedClass(Client.class);
            c.addAnnotatedClass(MachineType.class);
            c.addAnnotatedClass(Machine.class);
            c.addAnnotatedClass(RepairType.class);
            c.addAnnotatedClass(Repair.class);
            c.addAnnotatedClass(User.class);
            c.addAnnotatedClass(Role.class);
            c.addAnnotatedClass(UserRole.class);
            sessionFactory = c.buildSessionFactory();
        }
        return sessionFactory;
    }
}
