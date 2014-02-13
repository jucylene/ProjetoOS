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
import modelo.Cidade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import modelo.Endereco;
import modelo.Estado;

/**
 *
 * @author Helismara
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) throws PreexistingEntityException, Exception {
        if (estado.getCidadeCollection() == null) {
            estado.setCidadeCollection(new ArrayList<Cidade>());
        }
        if (estado.getEnderecoCollection() == null) {
            estado.setEnderecoCollection(new ArrayList<Endereco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Cidade> attachedCidadeCollection = new ArrayList<Cidade>();
            for (Cidade cidadeCollectionCidadeToAttach : estado.getCidadeCollection()) {
                cidadeCollectionCidadeToAttach = em.getReference(cidadeCollectionCidadeToAttach.getClass(), cidadeCollectionCidadeToAttach.getCodigo());
                attachedCidadeCollection.add(cidadeCollectionCidadeToAttach);
            }
            estado.setCidadeCollection(attachedCidadeCollection);
            Collection<Endereco> attachedEnderecoCollection = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionEnderecoToAttach : estado.getEnderecoCollection()) {
                enderecoCollectionEnderecoToAttach = em.getReference(enderecoCollectionEnderecoToAttach.getClass(), enderecoCollectionEnderecoToAttach.getCodigo());
                attachedEnderecoCollection.add(enderecoCollectionEnderecoToAttach);
            }
            estado.setEnderecoCollection(attachedEnderecoCollection);
            em.persist(estado);
            for (Cidade cidadeCollectionCidade : estado.getCidadeCollection()) {
                Estado oldSiglaEstadoOfCidadeCollectionCidade = cidadeCollectionCidade.getSiglaEstado();
                cidadeCollectionCidade.setSiglaEstado(estado);
                cidadeCollectionCidade = em.merge(cidadeCollectionCidade);
                if (oldSiglaEstadoOfCidadeCollectionCidade != null) {
                    oldSiglaEstadoOfCidadeCollectionCidade.getCidadeCollection().remove(cidadeCollectionCidade);
                    oldSiglaEstadoOfCidadeCollectionCidade = em.merge(oldSiglaEstadoOfCidadeCollectionCidade);
                }
            }
            for (Endereco enderecoCollectionEndereco : estado.getEnderecoCollection()) {
                Estado oldSiglaestadoOfEnderecoCollectionEndereco = enderecoCollectionEndereco.getSiglaestado();
                enderecoCollectionEndereco.setSiglaestado(estado);
                enderecoCollectionEndereco = em.merge(enderecoCollectionEndereco);
                if (oldSiglaestadoOfEnderecoCollectionEndereco != null) {
                    oldSiglaestadoOfEnderecoCollectionEndereco.getEnderecoCollection().remove(enderecoCollectionEndereco);
                    oldSiglaestadoOfEnderecoCollectionEndereco = em.merge(oldSiglaestadoOfEnderecoCollectionEndereco);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstado(estado.getSigla()) != null) {
                throw new PreexistingEntityException("Estado " + estado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getSigla());
            Collection<Cidade> cidadeCollectionOld = persistentEstado.getCidadeCollection();
            Collection<Cidade> cidadeCollectionNew = estado.getCidadeCollection();
            Collection<Endereco> enderecoCollectionOld = persistentEstado.getEnderecoCollection();
            Collection<Endereco> enderecoCollectionNew = estado.getEnderecoCollection();
            List<String> illegalOrphanMessages = null;
            for (Cidade cidadeCollectionOldCidade : cidadeCollectionOld) {
                if (!cidadeCollectionNew.contains(cidadeCollectionOldCidade)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cidade " + cidadeCollectionOldCidade + " since its siglaEstado field is not nullable.");
                }
            }
            for (Endereco enderecoCollectionOldEndereco : enderecoCollectionOld) {
                if (!enderecoCollectionNew.contains(enderecoCollectionOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoCollectionOldEndereco + " since its siglaestado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Cidade> attachedCidadeCollectionNew = new ArrayList<Cidade>();
            for (Cidade cidadeCollectionNewCidadeToAttach : cidadeCollectionNew) {
                cidadeCollectionNewCidadeToAttach = em.getReference(cidadeCollectionNewCidadeToAttach.getClass(), cidadeCollectionNewCidadeToAttach.getCodigo());
                attachedCidadeCollectionNew.add(cidadeCollectionNewCidadeToAttach);
            }
            cidadeCollectionNew = attachedCidadeCollectionNew;
            estado.setCidadeCollection(cidadeCollectionNew);
            Collection<Endereco> attachedEnderecoCollectionNew = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionNewEnderecoToAttach : enderecoCollectionNew) {
                enderecoCollectionNewEnderecoToAttach = em.getReference(enderecoCollectionNewEnderecoToAttach.getClass(), enderecoCollectionNewEnderecoToAttach.getCodigo());
                attachedEnderecoCollectionNew.add(enderecoCollectionNewEnderecoToAttach);
            }
            enderecoCollectionNew = attachedEnderecoCollectionNew;
            estado.setEnderecoCollection(enderecoCollectionNew);
            estado = em.merge(estado);
            for (Cidade cidadeCollectionNewCidade : cidadeCollectionNew) {
                if (!cidadeCollectionOld.contains(cidadeCollectionNewCidade)) {
                    Estado oldSiglaEstadoOfCidadeCollectionNewCidade = cidadeCollectionNewCidade.getSiglaEstado();
                    cidadeCollectionNewCidade.setSiglaEstado(estado);
                    cidadeCollectionNewCidade = em.merge(cidadeCollectionNewCidade);
                    if (oldSiglaEstadoOfCidadeCollectionNewCidade != null && !oldSiglaEstadoOfCidadeCollectionNewCidade.equals(estado)) {
                        oldSiglaEstadoOfCidadeCollectionNewCidade.getCidadeCollection().remove(cidadeCollectionNewCidade);
                        oldSiglaEstadoOfCidadeCollectionNewCidade = em.merge(oldSiglaEstadoOfCidadeCollectionNewCidade);
                    }
                }
            }
            for (Endereco enderecoCollectionNewEndereco : enderecoCollectionNew) {
                if (!enderecoCollectionOld.contains(enderecoCollectionNewEndereco)) {
                    Estado oldSiglaestadoOfEnderecoCollectionNewEndereco = enderecoCollectionNewEndereco.getSiglaestado();
                    enderecoCollectionNewEndereco.setSiglaestado(estado);
                    enderecoCollectionNewEndereco = em.merge(enderecoCollectionNewEndereco);
                    if (oldSiglaestadoOfEnderecoCollectionNewEndereco != null && !oldSiglaestadoOfEnderecoCollectionNewEndereco.equals(estado)) {
                        oldSiglaestadoOfEnderecoCollectionNewEndereco.getEnderecoCollection().remove(enderecoCollectionNewEndereco);
                        oldSiglaestadoOfEnderecoCollectionNewEndereco = em.merge(oldSiglaestadoOfEnderecoCollectionNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = estado.getSigla();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getSigla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cidade> cidadeCollectionOrphanCheck = estado.getCidadeCollection();
            for (Cidade cidadeCollectionOrphanCheckCidade : cidadeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Cidade " + cidadeCollectionOrphanCheckCidade + " in its cidadeCollection field has a non-nullable siglaEstado field.");
            }
            Collection<Endereco> enderecoCollectionOrphanCheck = estado.getEnderecoCollection();
            for (Endereco enderecoCollectionOrphanCheckEndereco : enderecoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Endereco " + enderecoCollectionOrphanCheckEndereco + " in its enderecoCollection field has a non-nullable siglaestado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
   public List<Estado> pesquisarPorSigla(String sigla) {
        EntityManager em = getEntityManager();

        TypedQuery<Estado> e;
        e = em.createQuery("select es from Estado es where es.sigla = :sigla",
             Estado.class);
        e.setParameter("sigla", sigla);
        
        return e.getResultList();
    }
    


    
}
