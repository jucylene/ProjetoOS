/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CidadeJpaController;
import dao.ClienteJpaController;
import dao.EnderecoJpaController;
import dao.EstadoJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Cidade;
import modelo.Cliente;
import modelo.Endereco;
import modelo.Estado;
import util.EMF;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class ClienteMB {

    private Cliente cliente = new Cliente();
    private ClienteJpaController clienteDao = new ClienteJpaController(EMF.getEntityManagerFactory());
    private EstadoJpaController estadoDao = new EstadoJpaController(EMF.getEntityManagerFactory());
    private CidadeJpaController cidadeDao = new CidadeJpaController(EMF.getEntityManagerFactory());
    private EnderecoJpaController enderecoDao = new EnderecoJpaController(EMF.getEntityManagerFactory());
    private Estado estado = new Estado();
    private Cidade cidade = new Cidade();
    private Endereco endereco = new Endereco();
    private List<Estado> estados = new ArrayList<Estado>();
    private List<Cidade> cidades = new ArrayList<Cidade>();
      
    private List<Cliente> clientes;
    
   
    
    
    public ClienteMB() {
        endereco = new Endereco();
        estado = new Estado();
        cidade = new Cidade();
    }
    
    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public CidadeJpaController getCidadeDao() {
        return cidadeDao;
    }

    public void setCidadeDao(CidadeJpaController cidadeDao) {
        this.cidadeDao = cidadeDao;
    }
    


    public EstadoJpaController getEstadoDao() {
        return estadoDao;
    }

    public void setEstadoDao(EstadoJpaController estadoDao) {
        this.estadoDao = estadoDao;
    }
    

    
    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ClienteJpaController getClienteDao() {
        return clienteDao;
    }

    public void setClienteDao(ClienteJpaController clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

      
    public List<Estado> getEstados() {
        estados = estadoDao.findEstadoEntities();
        return estados;
    }
    
    

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public List<Cidade> getCidades() {
        cidades = cidadeDao.findCidadeEntities();
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }
    
    public void cadastraCliente() throws PreexistingEntityException, Exception {
        endereco.setCodigocidade(cidade);
        endereco.setSiglaestado(estado);
        enderecoDao.create(endereco);
        cliente.setCodigoendereco(endereco);
        clienteDao.create(cliente);
    }
    

    public void alterarCliente() {
        try {
            clienteDao.edit(getCliente());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pesquisarCliente() {
        clienteDao.findClienteEntities();
    }

    public void excluirCliente(String cnpj) {
        try {
            clienteDao.destroy(cnpj);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
   
   
   public void carregarEstado(Estado es) {
        setEstado(es);
    }
   
  
    
    
   
}
