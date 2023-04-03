package com.sasha.servletapi.util;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.pojo.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.InputStream;
import java.util.Properties;

import static com.sasha.servletapi.util.constant.Constants.FAILED_DATABASE_CONNECTION;
import static com.sasha.servletapi.util.constant.Constants.HIBERNATE_PROPERTIES;

public class HibernateUtil {
    private static HibernateUtil instance;
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private HibernateUtil() {}
    public static HibernateUtil getInstance() {
        if (instance == null) {
            synchronized(HibernateUtil.class) {
                if (instance == null) {
                    instance = new HibernateUtil();
                }
            }
        }
        return instance;
    }

    private static SessionFactory buildSessionFactory() {
        try(InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream(HIBERNATE_PROPERTIES)){
            Properties properties = new Properties();
            properties.load(input);
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(properties)
                    .build();
            Metadata metadata = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Event.class)
                    .addAnnotatedClass(File.class)
                    .buildMetadata();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception e){
            throw new NotFoundException(FAILED_DATABASE_CONNECTION, e);
        }
    }

    public SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }
}

