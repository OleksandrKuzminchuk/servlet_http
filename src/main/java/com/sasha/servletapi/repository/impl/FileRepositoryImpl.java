package com.sasha.servletapi.repository.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.util.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.*;
import static com.sasha.servletapi.util.constant.SqlConstantsFile.*;

public class FileRepositoryImpl implements FileRepository {
    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();

    @Override
    public File save(File file) {
        File savedFile = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            savedFile = (File) session.merge(file);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_SAVE_FILE + e);
        }
        return savedFile;
    }

    @Override
    public File update(File file) {
        File updatedFile = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            updatedFile = session.get(File.class, file.getId(), LockMode.PESSIMISTIC_WRITE);
            updatedFile.setName(file.getName());
            updatedFile.setFilePath(file.getFilePath());
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_UPDATE_FILE + e.getMessage());
        }
        return updatedFile;

    }

    @Override
    public File findById(Integer id) {
        File file = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            file = session.get(File.class, id);
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_FIND_FILE_BY_ID + e.getMessage());
        }
        return file;
    }

    @Override
    public List<File> findAll() {
        List<File> files = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            files = session
                    .createQuery(FIND_ALL_FILES, File.class)
                    .getResultList();
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_FIND_ALL_FILES + e.getMessage());
        }
        return files;
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_FILE_BY_ID)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_DELETE_FILE_BY_ID + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_ALL_FILES)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_DELETE_ALL_FILES + e.getMessage());
        }
    }

    @Override
    public boolean isExistsByName(String name) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session
                    .createQuery(IS_EXISTS_FILE_BY_NAME, Long.class)
                    .setParameter("name", name);
            long count = query.getSingleResult();
            return count > 0;
        }
    }

    @Override
    public File findByName(String name) {
        File file = null;
        try(Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            file = session
                    .createQuery( FIND_BY_NAME, File.class)
                    .setParameter("name", name)
                    .getSingleResult();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(NOT_FOUND_FILE + e.getMessage());
        }
        return file;
    }
}
