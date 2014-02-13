/*
 * To change this template, choose Tools | Templates
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
import modelo.ProdutoServico;

/**
 *
 * @author Helismara
 */
public class ProdutoServicoJpaController implements Serializable {

    public ProdutoServicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProdutoServico produtoServico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(produtoServico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProdutoServico produtoServico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            produtoServico = em.merge(produtoServico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produtoServico.getCodigo();
                if (findProdutoServico(id) == null) {
                    throw new NonexistentEntityException("The produtoServico with id " + id + " no longer exists.");
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
            ProdutoServico produtoServico;
            try {
                produtoServico = em.getReference(ProdutoServico.class, id);
                produtoServico.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produtoServico with id " + id + " no longer exists.", enfe);
            }
            em.remove(produtoServico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProdutoServico> findProdutoServicoEntities() {
        return findProdutoServicoEntities(true, -1, -1);
    }

    public List<ProdutoServico> findProdutoServicoEntities(int maxResults, int firstResult) {
        return findProdutoServicoEntities(false, maxResults, firstResult);
    }

    private List<ProdutoServico> findProdutoServicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProdutoServico.class));
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

    public ProdutoServico findProdutoServico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdutoServico.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoServicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutoServico> rt = cq.from(ProdutoServico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
