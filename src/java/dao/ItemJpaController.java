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
import modelo.Item;
import modelo.ProdutoServico;
import modelo.Os;

/**
 *
 * @author Helismara
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutoServico codigoProdutoServico = item.getCodigoProdutoServico();
            if (codigoProdutoServico != null) {
                codigoProdutoServico = em.getReference(codigoProdutoServico.getClass(), codigoProdutoServico.getCodigo());
                item.setCodigoProdutoServico(codigoProdutoServico);
            }
            Os protocoloOs = item.getProtocoloOs();
            if (protocoloOs != null) {
                protocoloOs = em.getReference(protocoloOs.getClass(), protocoloOs.getProtocolo());
                item.setProtocoloOs(protocoloOs);
            }
            em.persist(item);
            if (codigoProdutoServico != null) {
                codigoProdutoServico.getItemCollection().add(item);
                codigoProdutoServico = em.merge(codigoProdutoServico);
            }
            if (protocoloOs != null) {
                protocoloOs.getItemCollection().add(item);
                protocoloOs = em.merge(protocoloOs);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getCodigo());
            ProdutoServico codigoProdutoServicoOld = persistentItem.getCodigoProdutoServico();
            ProdutoServico codigoProdutoServicoNew = item.getCodigoProdutoServico();
            Os protocoloOsOld = persistentItem.getProtocoloOs();
            Os protocoloOsNew = item.getProtocoloOs();
            if (codigoProdutoServicoNew != null) {
                codigoProdutoServicoNew = em.getReference(codigoProdutoServicoNew.getClass(), codigoProdutoServicoNew.getCodigo());
                item.setCodigoProdutoServico(codigoProdutoServicoNew);
            }
            if (protocoloOsNew != null) {
                protocoloOsNew = em.getReference(protocoloOsNew.getClass(), protocoloOsNew.getProtocolo());
                item.setProtocoloOs(protocoloOsNew);
            }
            item = em.merge(item);
            if (codigoProdutoServicoOld != null && !codigoProdutoServicoOld.equals(codigoProdutoServicoNew)) {
                codigoProdutoServicoOld.getItemCollection().remove(item);
                codigoProdutoServicoOld = em.merge(codigoProdutoServicoOld);
            }
            if (codigoProdutoServicoNew != null && !codigoProdutoServicoNew.equals(codigoProdutoServicoOld)) {
                codigoProdutoServicoNew.getItemCollection().add(item);
                codigoProdutoServicoNew = em.merge(codigoProdutoServicoNew);
            }
            if (protocoloOsOld != null && !protocoloOsOld.equals(protocoloOsNew)) {
                protocoloOsOld.getItemCollection().remove(item);
                protocoloOsOld = em.merge(protocoloOsOld);
            }
            if (protocoloOsNew != null && !protocoloOsNew.equals(protocoloOsOld)) {
                protocoloOsNew.getItemCollection().add(item);
                protocoloOsNew = em.merge(protocoloOsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getCodigo();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            ProdutoServico codigoProdutoServico = item.getCodigoProdutoServico();
            if (codigoProdutoServico != null) {
                codigoProdutoServico.getItemCollection().remove(item);
                codigoProdutoServico = em.merge(codigoProdutoServico);
            }
            Os protocoloOs = item.getProtocoloOs();
            if (protocoloOs != null) {
                protocoloOs.getItemCollection().remove(item);
                protocoloOs = em.merge(protocoloOs);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
