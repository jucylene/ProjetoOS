/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Os;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Cliente;

/**
 *
 * @author Helismara
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getOsCollection() == null) {
            cliente.setOsCollection(new ArrayList<Os>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Os> attachedOsCollection = new ArrayList<Os>();
            for (Os osCollectionOsToAttach : cliente.getOsCollection()) {
                osCollectionOsToAttach = em.getReference(osCollectionOsToAttach.getClass(), osCollectionOsToAttach.getProtocolo());
                attachedOsCollection.add(osCollectionOsToAttach);
            }
            cliente.setOsCollection(attachedOsCollection);
            em.persist(cliente);
            for (Os osCollectionOs : cliente.getOsCollection()) {
                Cliente oldCodigoclienteOfOsCollectionOs = osCollectionOs.getCodigocliente();
                osCollectionOs.setCodigocliente(cliente);
                osCollectionOs = em.merge(osCollectionOs);
                if (oldCodigoclienteOfOsCollectionOs != null) {
                    oldCodigoclienteOfOsCollectionOs.getOsCollection().remove(osCollectionOs);
                    oldCodigoclienteOfOsCollectionOs = em.merge(oldCodigoclienteOfOsCollectionOs);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getCnpj()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCnpj());
            Collection<Os> osCollectionOld = persistentCliente.getOsCollection();
            Collection<Os> osCollectionNew = cliente.getOsCollection();
            List<String> illegalOrphanMessages = null;
            for (Os osCollectionOldOs : osCollectionOld) {
                if (!osCollectionNew.contains(osCollectionOldOs)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Os " + osCollectionOldOs + " since its codigocliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Os> attachedOsCollectionNew = new ArrayList<Os>();
            for (Os osCollectionNewOsToAttach : osCollectionNew) {
                osCollectionNewOsToAttach = em.getReference(osCollectionNewOsToAttach.getClass(), osCollectionNewOsToAttach.getProtocolo());
                attachedOsCollectionNew.add(osCollectionNewOsToAttach);
            }
            osCollectionNew = attachedOsCollectionNew;
            cliente.setOsCollection(osCollectionNew);
            cliente = em.merge(cliente);
            for (Os osCollectionNewOs : osCollectionNew) {
                if (!osCollectionOld.contains(osCollectionNewOs)) {
                    Cliente oldCodigoclienteOfOsCollectionNewOs = osCollectionNewOs.getCodigocliente();
                    osCollectionNewOs.setCodigocliente(cliente);
                    osCollectionNewOs = em.merge(osCollectionNewOs);
                    if (oldCodigoclienteOfOsCollectionNewOs != null && !oldCodigoclienteOfOsCollectionNewOs.equals(cliente)) {
                        oldCodigoclienteOfOsCollectionNewOs.getOsCollection().remove(osCollectionNewOs);
                        oldCodigoclienteOfOsCollectionNewOs = em.merge(oldCodigoclienteOfOsCollectionNewOs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cliente.getCnpj();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCnpj();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Os> osCollectionOrphanCheck = cliente.getOsCollection();
            for (Os osCollectionOrphanCheckOs : osCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Os " + osCollectionOrphanCheckOs + " in its osCollection field has a non-nullable codigocliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
