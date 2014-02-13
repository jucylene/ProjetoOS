/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Estado;
import modelo.Cidade;
import modelo.Cliente;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Endereco;
import modelo.Funcionario;

/**
 *
 * @author Helismara
 */
public class EnderecoJpaController implements Serializable {

    public EnderecoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Endereco endereco) {
        if (endereco.getClienteCollection() == null) {
            endereco.setClienteCollection(new ArrayList<Cliente>());
        }
        if (endereco.getFuncionarioCollection() == null) {
            endereco.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado siglaestado = endereco.getSiglaestado();
            if (siglaestado != null) {
                siglaestado = em.getReference(siglaestado.getClass(), siglaestado.getSigla());
                endereco.setSiglaestado(siglaestado);
            }
            Cidade codigocidade = endereco.getCodigocidade();
            if (codigocidade != null) {
                codigocidade = em.getReference(codigocidade.getClass(), codigocidade.getCodigo());
                endereco.setCodigocidade(codigocidade);
            }
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : endereco.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getCnpj());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            endereco.setClienteCollection(attachedClienteCollection);
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : endereco.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getCpf());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            endereco.setFuncionarioCollection(attachedFuncionarioCollection);
            em.persist(endereco);
            if (siglaestado != null) {
                siglaestado.getEnderecoCollection().add(endereco);
                siglaestado = em.merge(siglaestado);
            }
            if (codigocidade != null) {
                codigocidade.getEnderecoCollection().add(endereco);
                codigocidade = em.merge(codigocidade);
            }
            for (Cliente clienteCollectionCliente : endereco.getClienteCollection()) {
                Endereco oldCodigoenderecoOfClienteCollectionCliente = clienteCollectionCliente.getCodigoendereco();
                clienteCollectionCliente.setCodigoendereco(endereco);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
                if (oldCodigoenderecoOfClienteCollectionCliente != null) {
                    oldCodigoenderecoOfClienteCollectionCliente.getClienteCollection().remove(clienteCollectionCliente);
                    oldCodigoenderecoOfClienteCollectionCliente = em.merge(oldCodigoenderecoOfClienteCollectionCliente);
                }
            }
            for (Funcionario funcionarioCollectionFuncionario : endereco.getFuncionarioCollection()) {
                Endereco oldCodigoenderecoOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getCodigoendereco();
                funcionarioCollectionFuncionario.setCodigoendereco(endereco);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldCodigoenderecoOfFuncionarioCollectionFuncionario != null) {
                    oldCodigoenderecoOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldCodigoenderecoOfFuncionarioCollectionFuncionario = em.merge(oldCodigoenderecoOfFuncionarioCollectionFuncionario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Endereco endereco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getCodigo());
            Estado siglaestadoOld = persistentEndereco.getSiglaestado();
            Estado siglaestadoNew = endereco.getSiglaestado();
            Cidade codigocidadeOld = persistentEndereco.getCodigocidade();
            Cidade codigocidadeNew = endereco.getCodigocidade();
            Collection<Cliente> clienteCollectionOld = persistentEndereco.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = endereco.getClienteCollection();
            Collection<Funcionario> funcionarioCollectionOld = persistentEndereco.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = endereco.getFuncionarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteCollectionOldCliente + " since its codigoendereco field is not nullable.");
                }
            }
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioCollectionOldFuncionario + " since its codigoendereco field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (siglaestadoNew != null) {
                siglaestadoNew = em.getReference(siglaestadoNew.getClass(), siglaestadoNew.getSigla());
                endereco.setSiglaestado(siglaestadoNew);
            }
            if (codigocidadeNew != null) {
                codigocidadeNew = em.getReference(codigocidadeNew.getClass(), codigocidadeNew.getCodigo());
                endereco.setCodigocidade(codigocidadeNew);
            }
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getCnpj());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            endereco.setClienteCollection(clienteCollectionNew);
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getCpf());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            endereco.setFuncionarioCollection(funcionarioCollectionNew);
            endereco = em.merge(endereco);
            if (siglaestadoOld != null && !siglaestadoOld.equals(siglaestadoNew)) {
                siglaestadoOld.getEnderecoCollection().remove(endereco);
                siglaestadoOld = em.merge(siglaestadoOld);
            }
            if (siglaestadoNew != null && !siglaestadoNew.equals(siglaestadoOld)) {
                siglaestadoNew.getEnderecoCollection().add(endereco);
                siglaestadoNew = em.merge(siglaestadoNew);
            }
            if (codigocidadeOld != null && !codigocidadeOld.equals(codigocidadeNew)) {
                codigocidadeOld.getEnderecoCollection().remove(endereco);
                codigocidadeOld = em.merge(codigocidadeOld);
            }
            if (codigocidadeNew != null && !codigocidadeNew.equals(codigocidadeOld)) {
                codigocidadeNew.getEnderecoCollection().add(endereco);
                codigocidadeNew = em.merge(codigocidadeNew);
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    Endereco oldCodigoenderecoOfClienteCollectionNewCliente = clienteCollectionNewCliente.getCodigoendereco();
                    clienteCollectionNewCliente.setCodigoendereco(endereco);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                    if (oldCodigoenderecoOfClienteCollectionNewCliente != null && !oldCodigoenderecoOfClienteCollectionNewCliente.equals(endereco)) {
                        oldCodigoenderecoOfClienteCollectionNewCliente.getClienteCollection().remove(clienteCollectionNewCliente);
                        oldCodigoenderecoOfClienteCollectionNewCliente = em.merge(oldCodigoenderecoOfClienteCollectionNewCliente);
                    }
                }
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Endereco oldCodigoenderecoOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getCodigoendereco();
                    funcionarioCollectionNewFuncionario.setCodigoendereco(endereco);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldCodigoenderecoOfFuncionarioCollectionNewFuncionario != null && !oldCodigoenderecoOfFuncionarioCollectionNewFuncionario.equals(endereco)) {
                        oldCodigoenderecoOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldCodigoenderecoOfFuncionarioCollectionNewFuncionario = em.merge(oldCodigoenderecoOfFuncionarioCollectionNewFuncionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getCodigo();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
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
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cliente> clienteCollectionOrphanCheck = endereco.getClienteCollection();
            for (Cliente clienteCollectionOrphanCheckCliente : clienteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Endereco (" + endereco + ") cannot be destroyed since the Cliente " + clienteCollectionOrphanCheckCliente + " in its clienteCollection field has a non-nullable codigoendereco field.");
            }
            Collection<Funcionario> funcionarioCollectionOrphanCheck = endereco.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionOrphanCheckFuncionario : funcionarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Endereco (" + endereco + ") cannot be destroyed since the Funcionario " + funcionarioCollectionOrphanCheckFuncionario + " in its funcionarioCollection field has a non-nullable codigoendereco field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado siglaestado = endereco.getSiglaestado();
            if (siglaestado != null) {
                siglaestado.getEnderecoCollection().remove(endereco);
                siglaestado = em.merge(siglaestado);
            }
            Cidade codigocidade = endereco.getCodigocidade();
            if (codigocidade != null) {
                codigocidade.getEnderecoCollection().remove(endereco);
                codigocidade = em.merge(codigocidade);
            }
            em.remove(endereco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
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

    public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
