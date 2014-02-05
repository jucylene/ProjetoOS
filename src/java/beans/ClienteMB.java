/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ClienteJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Cliente;
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

    public ClienteMB() {
    }

    public void cadastraCliente() {
        clienteDao.create(getCliente());
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

    public void excluirCliente(Integer codigo) {
        try {
            clienteDao.destroy(codigo);
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

}
