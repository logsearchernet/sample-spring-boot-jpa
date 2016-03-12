package com.sample.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseDao<E extends Serializable, K> {
	
	
	@PersistenceContext
	protected EntityManager em;
	
	private Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDao() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		this.entityClass = (Class<E>) params[0];
	}

	
	    public void create(E e) {
	        em.persist(e);
	        em.flush();
	    }

	   
	    public void update(E e) {
	        em.merge(e);
	        em.flush();
	    }
	    

	   
	    public void delete(K id) {
	        E delete = em.getReference(entityClass, id);

	        em.remove(delete);
	        em.flush();
	    }

	   
	    public E find(K id) {
	        return em.find(entityClass, id);
	    }

	   
	    public void createBatch(Collection<E> e) {
	        
	        int i = 0;
	        for (E ent : e) {
	            em.persist(ent);

	            if (i % 20 == 0) {
	                em.flush();
	            }
	        }
	        em.flush();

	        i++;
	    }

	   
	    public void updateBatch(Collection<E> e) {
	        
	        int i = 0;
	        for (E ent : e) {
	            em.merge(ent);

	            if (i % 20 == 0) {
	                em.flush();
	            }
	        }
	        em.flush();

	        i++;
	    }

	   
	    public void deleteBatch(Collection<K> ids) {
	        
	        int i = 0;
	        for (K id : ids) {
	            E delete = em.getReference(entityClass, id);
	            em.remove(delete);

	            if (i % 20 == 0) {
	                em.flush();
	            }
	        }
	        em.flush();

	        i++;
	    }

	   
	    public List<E> findAll() {
	        CriteriaQuery<E> cq = em.getCriteriaBuilder().createQuery(entityClass);
	        Root<E> root = cq.from(entityClass);
	        return em.createQuery(cq.select(root)).getResultList();
	    }

	   
	    public Long countAll() {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	        cq.select(cb.count(cq.from(entityClass)));
	        return em.createQuery(cq).getSingleResult();
	    }

	   
	    public List<E> findWithNamedQuery(String named, Map<String, Object> params) {
	        TypedQuery<E> q = em.createNamedQuery(named, entityClass);

	        if (params != null && !params.isEmpty()) {
	            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
	                q.setParameter(entrySet.getKey(), entrySet.getValue());
	            }
	        }

	        return q.getResultList();
	    }

	   
	    public E findSingleWithNamedQuery(String named, Map<String, Object> params) {
	        TypedQuery<E> q = em.createNamedQuery(named, entityClass);

	        if (params != null && !params.isEmpty()) {
	            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
	                q.setParameter(entrySet.getKey(), entrySet.getValue());
	            }
	        }

	        List<E> results = q.getResultList();
	        return results.isEmpty() ? null : results.get(0);
	    }

	   
	    public Long countWithNamedQuery(String named, Map<String, Object> params) {
	        TypedQuery<Long> q = em.createNamedQuery(named, Long.class);

	        if (params != null && !params.isEmpty()) {
	            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
	                q.setParameter(entrySet.getKey(), entrySet.getValue());
	            }
	        }
	       String s = q.unwrap(org.hibernate.Query.class).getQueryString();
	       System.out.println("getQueryString="+s);
	        return q.getSingleResult();
	    }

	   
	    public <T> List<T> findAggregateWithNamedQuery(String namedQuery, int limit, Map<String, Object> params, Class<T> type) {
	        if (namedQuery != null && !namedQuery.isEmpty() && type != null) {
	            Query q = em.createNamedQuery(namedQuery, type);

	            if (params != null && !params.isEmpty()) {
	                for (Map.Entry<String, Object> entrySet : params.entrySet()) {
	                    q.setParameter(entrySet.getKey(), entrySet.getValue());
	                }
	            }
	            
	            return limit > 0 ? q.setMaxResults(limit).getResultList() : q.getResultList();
	        } else {
	            throw new IllegalArgumentException("Query o tipo nulos");
	        }
	    }

	   
	    public <T> List<T> doWithNativeQuery(String query, Object[] params, Class<T> type) {
	        if (query != null && !query.isEmpty() && type != null) {
	            Query q = em.createNativeQuery(query, type);

	            if (params != null && params.length > 0) {
	                for (int i = 0; i < params.length; i++) {
	                    q.setParameter(i + 1, params[i]);
	                }
	            }

	            return q.getResultList();
	        } else {
	            throw new IllegalArgumentException("Query o tipo nulos");
	        }
	    }

	   
	    public List<E> findLazy(int firstRow, int numRows, Map<String, Boolean> sort, Map<String, Object> filters, Map<String, String> hints) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<E> cq = cb.createQuery(entityClass);
	        Root<E> root = cq.from(entityClass);

	        if (sort != null && !sort.isEmpty()) {
	            List<Order> orders = new ArrayList<Order>();
	            for (Map.Entry<String, Boolean> sortField : sort.entrySet()) {
	                if (sortField.getValue()) {
	                    orders.add(cb.asc(root.get(sortField.getKey())));
	                } else {
	                    orders.add(cb.desc(root.get(sortField.getKey())));
	                }
	            }

	            cq.orderBy(orders);
	        }

	        Predicate f = cb.conjunction();
	        if (filters != null && !filters.isEmpty()) {
	            for (Map.Entry<String, Object> filter : filters.entrySet()) {
	                if (filter.getValue() instanceof String) {
	                    if (!filter.getValue().equals("")) {
	                        String valueString = (String) filter.getValue();
	                        f = cb.and(f, cb.like(cb.upper(root.<String>get(filter.getKey())), "%" + valueString.toUpperCase() + "%"));
	                    }
	                } else {
	                    //Seguramente no es string.
	                    f = cb.and(f, cb.equal(root.get(filter.getKey()), filter.getValue()));
	                }
	            }
	        }

	        cq.where(f);
	        TypedQuery<E> q = em.createQuery(cq);

	        if (firstRow > -1 && numRows > -1) {
	            q.setFirstResult(firstRow);
	            q.setMaxResults(numRows);
	        }

	        if (hints != null && !hints.isEmpty()) {
	            for (Map.Entry<String, String> hint : hints.entrySet()) {
	                q.setHint(hint.getKey(), hint.getValue());
	            }
	        }

	        return q.getResultList();
	    }
	
}
