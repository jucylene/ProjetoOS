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
import modelo.Endereco;
import modelo.Os;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Funcionario;

/**
 *
 * @author Helismara
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) throws PreexistingEntityException, Exception {
        if (funcionario.getOsCollection() == null) {
            funcionario.setOsCollection(new ArrayList<Os>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco codigoendereco = funcionario.getCodigoendereco();
            if (codigoendereco != null) {
                codigoendereco = em.getReference(codigoendereco.getClass(), codigoendereco.getCodigo());
                funcionario.setCodigoendereco(codigoendereco);
            }
            Collection<Os> attachedOsCollection = new ArrayList<Os>();
            for (Os osCollectionOsToAttach : funcionario.getOsCollection()) {
                osCollectionOsToAttach = em.getReference(osCollectionOsToAttach.getClass(), osCollectionOsToAttach.getProtocolo());
                attachedOsCollection.add(osCollectionOsToAttach);
            }
            funcionario.setOsCollection(attachedOsCollection);
            em.persist(funcionario);
            if (codigoendereco != null) {
                codigoendereco.getFuncionarioCollection().add(funcionario);
                codigoendereco = em.merge(codigoendereco);
            }
            for (Os osCollectionOs : funcionario.getOsCollection()) {
                Funcionario oldCodigofuncOfOsCollectionOs = osCollectionOs.getCodigofunc();
                osCollectionOs.setCodigofunc(funcionario);
                osCollectionOs = em.merge(osCollectionOs);
                if (oldCodigofuncOfOsCollectionOs != null) {
                    oldCodigofuncOfOsCollectionOs.getOsCollection().remove(osCollectionOs);
                    oldCodigofuncOfOsCollectionOs = em.merge(oldCodigofuncOfOsCollectionOs);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFuncionario(funcionario.getCpf()) != null) {
                throw new PreexistingEntityException("Funcionario " + funcionario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getCpf());
            Endereco codigoenderecoOld = persistentFuncionario.getCodigoendereco();
            Endereco codigoenderecoNew = funcionario.getCodigoendereco();
            Collection<Os> osCollectionOld = persistentFuncionario.getOsCollection();
            Collection<Os> osCollectionNew = funcionario.getOsCollection();
            List<String> illegalOrphanMessages = null;
            for (Os osCollectionOldOs : osCollectionOld) {
                if (!osCollectionNew.contains(osCollectionOldOs)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Os " + osCollectionOldOs + " since its codigofunc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoenderecoNew != null) {
                codigoenderecoNew = em.getReference(codigoenderecoNew.getClass(), codigoenderecoNew.getCodigo());
                funcionario.setCodigoendereco(codigoenderecoNew);
            }
            Collection<Os> attachedOsCollectionNew = new ArrayList<Os>();
            for (Os osCollectionNewOsToAttach : osCollectionNew) {
                osCollectionNewOsToAttach = em.getReference(osCollectionNewOsToAttach.getClass(), osCollectionNewOsToAttach.getProtocolo());
                attachedOsCollectionNew.add(osCollectionNewOsToAttach);
            }
            osCollectionNew = attachedOsCollectionNew;
            funcionario.setOsCollection(osCollectionNew);
            funcionario = em.merge(funcionario);
            if (codigoenderecoOld != null && !codigoenderecoOld.equals(codigoenderecoNew)) {
                codigoenderecoOld.getFuncionarioCollection().remove(funcionario);
                codigoenderecoOld = em.merge(codigoenderecoOld);
            }
            if (codigoenderecoNew != null && !codigoenderecoNew.equals(codigoenderecoOld)) {
                codigoenderecoNew.getFuncionarioCollection().add(funcionario);
                codigoenderecoNew = em.merge(codigoenderecoNew);
            }
            for (Os osCollectionNewOs : osCollectionNew) {
                if (!osCollectionOld.contains(osCollectionNewOs)) {
                    Funcionario oldCodigofuncOfOsCollectionNewOs = osCollectionNewOs.getCodigofunc();
                    osCollectionNewOs.setCodigofunc(funcionario);
                    osCollectionNewOs = em.merge(osCollectionNewOs);
                    if (oldCodigofuncOfOsCollectionNewOs != null && !oldCodigofuncOfOsCollectionNewOs.equals(funcionario)) {
                        oldCodigofuncOfOsCollectionNewOs.getOsCollection().remove(osCollectionNewOs);
                        oldCodigofuncOfOsCollectionNewOs = em.merge(oldCodigofuncOfOsCollectionNewOs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = funcionario.getCpf();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
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
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getCpf();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Os> osCollectionOrphanCheck = funcionario.getOsCollection();
            for (Os osCollectionOrphanCheckOs : osCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcionario (" + funcionario + ") cannot be destroyed since the Os " + osCollectionOrphanCheckOs + " in its osCollection field has a non-nullable codigofunc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Endereco codigoendereco = funcionario.getCodigoendereco();
            if (codigoendereco != null) {
                codigoendereco.getFuncionarioCollection().remove(funcionario);
                codigoendereco = em.merge(codigoendereco);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
