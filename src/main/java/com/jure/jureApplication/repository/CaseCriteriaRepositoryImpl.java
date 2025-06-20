package com.jure.jureApplication.repository;


import com.jure.common.persistant.model.Cabinet;
import com.jure.common.persistant.model.User;
import com.jure.jureApplication.persistent.dto.CaseCriteria;
import com.jure.jureApplication.persistent.model.Case;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CaseCriteriaRepositoryImpl implements CaseCriteriaRepository {

    @Autowired
    private EntityManager em;

    @Override
    public Page<Case> findByCriteria(CaseCriteria criteria, Long cabinetId, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Case> cq = cb.createQuery(Case.class);
        Root<Case> caseRoot = cq.from(Case.class);

        // Predicates for the main query
        List<Predicate> mainPredicates = generateWhere(criteria, cabinetId, cb, caseRoot);
        cq.where(mainPredicates.toArray(new Predicate[0]));

        // Add sorting if Pageable contains sort information
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
                Path<?> path = getPath(caseRoot, order.getProperty());
                if (path != null) {
                    if (order.isAscending()) {
                        orders.add(cb.asc(path));
                    } else {
                        orders.add(cb.desc(path));
                    }
                }
            }
            if (!orders.isEmpty()) {
                cq.orderBy(orders);
            }
        }

        // Pagination for the main query
        TypedQuery<Case> query = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // Count query for pagination
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Case> countRoot = countQuery.from(Case.class);
        List<Predicate> countPredicates = generateWhere(criteria, cabinetId, cb, countRoot);
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    @Override
    public Page<Case> findByCriteriaNotPaged(CaseCriteria criteria, Long cabinetId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Main query
        CriteriaQuery<Case> cq = cb.createQuery(Case.class);
        Root<Case> caseRoot = cq.from(Case.class);
        List<Predicate> predicatesMain = generateWhere(criteria, cabinetId, cb, caseRoot);
        cq.where(predicatesMain.toArray(new Predicate[0]));

        TypedQuery<Case> query = em.createQuery(cq);
        List<Case> content = query.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Case> countRoot = countQuery.from(Case.class);
        List<Predicate> predicatesCount = generateWhere(criteria, cabinetId, cb, countRoot);
        countQuery.select(cb.count(countRoot)).where(predicatesCount.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, Pageable.unpaged(), total);
    }

    private static List<Predicate> generateWhere(CaseCriteria criteria, Long cabinetId, CriteriaBuilder cb, Root<Case> caseRoot) {
        List<Predicate> predicates = new ArrayList<>();

        // Toujours filtrer par cabinet (sécurité multi-tenant)
        Join<Case, Cabinet> cabinetJoin = caseRoot.join("cabinet");
        predicates.add(cb.equal(cabinetJoin.get("id"), cabinetId));

        // Filtrer par référence
        if (StringUtils.hasText(criteria.getReference())) {
            predicates.add(cb.like(
                    cb.lower(caseRoot.get("reference")),
                    "%" + criteria.getReference().toLowerCase() + "%"
            ));
        }

        // Filtrer par titre
        if (StringUtils.hasText(criteria.getTitle())) {
            predicates.add(cb.like(
                    cb.lower(caseRoot.get("title")),
                    "%" + criteria.getTitle().toLowerCase() + "%"
            ));
        }

        // Filtrer par statut
        if (StringUtils.hasText(criteria.getStatus())) {
            predicates.add(cb.equal(caseRoot.get("status"), criteria.getStatus()));
        }

        // Filtrer par date d'audience
        if (criteria.getCourtDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(caseRoot.get("courtDate"), criteria.getCourtDateFrom()));
        }
        if (criteria.getCourtDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(caseRoot.get("courtDate"), criteria.getCourtDateTo()));
        }

        // Filtrer par utilisateur assigné
        if (criteria.getAssignedUserId() != null) {
            Join<Case, User> userJoin = caseRoot.join("assignedUser");
            predicates.add(cb.equal(userJoin.get("id"), criteria.getAssignedUserId()));
        }

        // Filtrer par nom du client
        if (StringUtils.hasText(criteria.getClientName())) {
            predicates.add(cb.like(
                    cb.lower(caseRoot.get("clientName")),
                    "%" + criteria.getClientName().toLowerCase() + "%"
            ));
        }

        return predicates;
    }

    private static Path<?> getPath(Root<?> root, String propertyPath) {
        Path<?> path = root;
        for (String part : propertyPath.split("\\.")) {
            try {
                path = path.get(part);
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Could not resolve path for sorting: " + propertyPath);
                return null;
            }
        }
        return path;
    }
}