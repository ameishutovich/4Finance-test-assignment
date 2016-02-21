package com.meishuto.common.persistence.dao.impl;

import com.meishuto.common.persistence.dao.ApplicationDAO;
import com.meishuto.common.persistence.domain.Application;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class ApplicationDAOImpl implements ApplicationDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Application saveApplication(Application application) {
        if (application.getId() == null) {
            em.persist(application);
            return application;
        } else {
            return em.merge(application);
        }
    }

    @Override
    public Long countApplicationsFromPeriod(Date from, Date to, String ipAddress, Integer... statuses) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Application> root = criteria.from(Application.class);

        Predicate pred1 = builder.between(root.<Date>get("time"), from, to);
        Predicate pred2 = builder.equal(root.get("user").get("ipAddress"), ipAddress);
        criteria.select(builder.count(root)).where(builder.and(pred1, pred2));

        if(statuses != null && statuses.length > 0) {
            Predicate pred = root.get("loan").get("status").in(statuses);
            criteria.where(pred);
        }
        return em.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<Application> getApplicationsOfUser(String ipAddress) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
        Root<Application> root = criteria.from(Application.class);
        root.fetch("loan");

        Predicate pred = builder.equal(root.get("user").get("ipAddress"), ipAddress);

        criteria.select(root).where(pred);
        return em.createQuery(criteria).getResultList();
    }
}
