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
import modelo.Estado;
import modelo.Endereco;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import modelo.Cidade;

/**
 *
 * @author Helismara
 */
public class CidadeJpaController implements Serializable {

    public CidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cidade cidade) throws PreexistingEntityException, Exception {
        if (cidade.getEnderecoCollection() == null) {
            cidade.setEnderecoCollection(new ArrayList<Endereco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado siglaEstado = cidade.getSiglaEstado();
            if (siglaEstado != null) {
                siglaEstado = em.getReference(siglaEstado.getClass(), siglaEstado.getSigla());
                cidade.setSiglaEstado(siglaEstado);
            }
            Collection<Endereco> attachedEnderecoCollection = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionEnderecoToAttach : cidade.getEnderecoCollection()) {
                enderecoCollectionEnderecoToAttach = em.getReference(enderecoCollectionEnderecoToAttach.getClass(), enderecoCollectionEnderecoToAttach.getCodigo());
                attachedEnderecoCollection.add(enderecoCollectionEnderecoToAttach);
            }
            cidade.setEnderecoCollection(attachedEnderecoCollection);
            em.persist(cidade);
            if (siglaEstado != null) {
                siglaEstado.getCidadeCollection().add(cidade);
                siglaEstado = em.merge(siglaEstado);
            }
            for (Endereco enderecoCollectionEndereco : cidade.getEnderecoCollection()) {
                Cidade oldCodigocidadeOfEnderecoCollectionEndereco = enderecoCollectionEndereco.getCodigocidade();
                enderecoCollectionEndereco.setCodigocidade(cidade);
                enderecoCollectionEndereco = em.merge(enderecoCollectionEndereco);
                if (oldCodigocidadeOfEnderecoCollectionEndereco != null) {
                    oldCodigocidadeOfEnderecoCollectionEndereco.getEnderecoCollection().remove(enderecoCollectionEndereco);
                    oldCodigocidadeOfEnderecoCollectionEndereco = em.merge(oldCodigocidadeOfEnderecoCollectionEndereco);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCidade(cidade.getCodigo()) != null) {
                throw new PreexistingEntityException("Cidade " + cidade + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cidade cidade) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade persistentCidade = em.find(Cidade.class, cidade.getCodigo());
            Estado siglaEstadoOld = persistentCidade.getSiglaEstado();
            Estado siglaEstadoNew = cidade.getSiglaEstado();
            Collection<Endereco> enderecoCollectionOld = persistentCidade.getEnderecoCollection();
            Collection<Endereco> enderecoCollectionNew = cidade.getEnderecoCollection();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecoCollectionOldEndereco : enderecoCollectionOld) {
                if (!enderecoCollectionNew.contains(enderecoCollectionOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoCollectionOldEndereco + " since its codigocidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (siglaEstadoNew != null) {
                siglaEstadoNew = em.getReference(siglaEstadoNew.getClass(), siglaEstadoNew.getSigla());
                cidade.setSiglaEstado(siglaEstadoNew);
            }
            Collection<Endereco> attachedEnderecoCollectionNew = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionNewEnderecoToAttach : enderecoCollectionNew) {
                enderecoCollectionNewEnderecoToAttach = em.getReference(enderecoCollectionNewEnderecoToAttach.getClass(), enderecoCollectionNewEnderecoToAttach.getCodigo());
                attachedEnderecoCollectionNew.add(enderecoCollectionNewEnderecoToAttach);
            }
            enderecoCollectionNew = attachedEnderecoCollectionNew;
            cidade.setEnderecoCollection(enderecoCollectionNew);
            cidade = em.merge(cidade);
            if (siglaEstadoOld != null && !siglaEstadoOld.equals(siglaEstadoNew)) {
                siglaEstadoOld.getCidadeCollection().remove(cidade);
                siglaEstadoOld = em.merge(siglaEstadoOld);
            }
            if (siglaEstadoNew != null && !siglaEstadoNew.equals(siglaEstadoOld)) {
                siglaEstadoNew.getCidadeCollection().add(cidade);
                siglaEstadoNew = em.merge(siglaEstadoNew);
            }
            for (Endereco enderecoCollectionNewEndereco : enderecoCollectionNew) {
                if (!enderecoCollectionOld.contains(enderecoCollectionNewEndereco)) {
                    Cidade oldCodigocidadeOfEnderecoCollectionNewEndereco = enderecoCollectionNewEndereco.getCodigocidade();
                    enderecoCollectionNewEndereco.setCodigocidade(cidade);
                    enderecoCollectionNewEndereco = em.merge(enderecoCollectionNewEndereco);
                    if (oldCodigocidadeOfEnderecoCollectionNewEndereco != null && !oldCodigocidadeOfEnderecoCollectionNewEndereco.equals(cidade)) {
                        oldCodigocidadeOfEnderecoCollectionNewEndereco.getEnderecoCollection().remove(enderecoCollectionNewEndereco);
                        oldCodigocidadeOfEnderecoCollectionNewEndereco = em.merge(oldCodigocidadeOfEnderecoCollectionNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cidade.getCodigo();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade cidade;
            try {
                cidade = em.getReference(Cidade.class, id);
                cidade.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Endereco> enderecoCollectionOrphanCheck = cidade.getEnderecoCollection();
            for (Endereco enderecoCollectionOrphanCheckEndereco : enderecoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cidade (" + cidade + ") cannot be destroyed since the Endereco " + enderecoCollectionOrphanCheckEndereco + " in its enderecoCollection field has a non-nullable codigocidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado siglaEstado = cidade.getSiglaEstado();
            if (siglaEstado != null) {
                siglaEstado.getCidadeCollection().remove(cidade);
                siglaEstado = em.merge(siglaEstado);
            }
            em.remove(cidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cidade> findCidadeEntities() {
        return findCidadeEntities(true, -1, -1);
    }

    public List<Cidade> findCidadeEntities(int maxResults, int firstResult) {
        return findCidadeEntities(false, maxResults, firstResult);
    }

    private List<Cidade> findCidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cidade.class));
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

    public Cidade findCidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getCidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cidade> rt = cq.from(Cidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     
    
}
