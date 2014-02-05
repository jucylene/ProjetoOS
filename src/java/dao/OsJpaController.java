/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Funcionario;
import modelo.Cliente;
import modelo.Os;

/**
 *
 * @author Helismara
 */
public class OsJpaController implements Serializable {

    public OsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Os os) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario codigofunc = os.getCodigofunc();
            if (codigofunc != null) {
                codigofunc = em.getReference(codigofunc.getClass(), codigofunc.getCodigo());
                os.setCodigofunc(codigofunc);
            }
            Cliente codigocliente = os.getCodigocliente();
            if (codigocliente != null) {
                codigocliente = em.getReference(codigocliente.getClass(), codigocliente.getCodigo());
                os.setCodigocliente(codigocliente);
            }
            em.persist(os);
            if (codigofunc != null) {
                codigofunc.getOsCollection().add(os);
                codigofunc = em.merge(codigofunc);
            }
            if (codigocliente != null) {
                codigocliente.getOsCollection().add(os);
                codigocliente = em.merge(codigocliente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Os os) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Os persistentOs = em.find(Os.class, os.getProtocolo());
            Funcionario codigofuncOld = persistentOs.getCodigofunc();
            Funcionario codigofuncNew = os.getCodigofunc();
            Cliente codigoclienteOld = persistentOs.getCodigocliente();
            Cliente codigoclienteNew = os.getCodigocliente();
            if (codigofuncNew != null) {
                codigofuncNew = em.getReference(codigofuncNew.getClass(), codigofuncNew.getCodigo());
                os.setCodigofunc(codigofuncNew);
            }
            if (codigoclienteNew != null) {
                codigoclienteNew = em.getReference(codigoclienteNew.getClass(), codigoclienteNew.getCodigo());
                os.setCodigocliente(codigoclienteNew);
            }
            os = em.merge(os);
            if (codigofuncOld != null && !codigofuncOld.equals(codigofuncNew)) {
                codigofuncOld.getOsCollection().remove(os);
                codigofuncOld = em.merge(codigofuncOld);
            }
            if (codigofuncNew != null && !codigofuncNew.equals(codigofuncOld)) {
                codigofuncNew.getOsCollection().add(os);
                codigofuncNew = em.merge(codigofuncNew);
            }
            if (codigoclienteOld != null && !codigoclienteOld.equals(codigoclienteNew)) {
                codigoclienteOld.getOsCollection().remove(os);
                codigoclienteOld = em.merge(codigoclienteOld);
            }
            if (codigoclienteNew != null && !codigoclienteNew.equals(codigoclienteOld)) {
                codigoclienteNew.getOsCollection().add(os);
                codigoclienteNew = em.merge(codigoclienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = os.getProtocolo();
                if (findOs(id) == null) {
                    throw new NonexistentEntityException("The os with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Os os;
            try {
                os = em.getReference(Os.class, id);
                os.getProtocolo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The os with id " + id + " no longer exists.", enfe);
            }
            Funcionario codigofunc = os.getCodigofunc();
            if (codigofunc != null) {
                codigofunc.getOsCollection().remove(os);
                codigofunc = em.merge(codigofunc);
            }
            Cliente codigocliente = os.getCodigocliente();
            if (codigocliente != null) {
                codigocliente.getOsCollection().remove(os);
                codigocliente = em.merge(codigocliente);
            }
            em.remove(os);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Os> findOsEntities() {
        return findOsEntities(true, -1, -1);
    }

    public List<Os> findOsEntities(int maxResults, int firstResult) {
        return findOsEntities(false, maxResults, firstResult);
    }

    private List<Os> findOsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Os.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Os findOs(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Os.class, id);
        } finally {
            em.close();
        }
    }

    public int getOsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Os> rt = cq.from(Os.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
