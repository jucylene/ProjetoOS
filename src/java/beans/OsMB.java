/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.ClienteJpaController;
import dao.FuncionarioJpaController;
import dao.OsJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Os;
import util.EMF;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class OsMB {

    private Os os = new Os();
    private Cliente cliente = new Cliente();
    private Funcionario funcionario = new Funcionario();
    private OsJpaController osDao = new OsJpaController(EMF.getEntityManagerFactory());
    private ClienteJpaController clienteDao = new ClienteJpaController(EMF.getEntityManagerFactory());
    private FuncionarioJpaController funcionarioDao = new FuncionarioJpaController(EMF.getEntityManagerFactory());
    private List<Cliente> clientes = new ArrayList<Cliente>();
    private List<Funcionario> funcionarios = new ArrayList<Funcionario>();
    private String cnpj;
    private String cpf;
    private Cliente gravarCliente;
    private Funcionario gravarFuncionario;
    
    
    
    
    public void retornaCliente()
    {
        for(Cliente cli : clienteDao.findClienteEntities())
        {
            if(cli.getCnpj().equals(cnpj))
            {
                gravarCliente = cli;
                os.setCodigocliente(gravarCliente);
            }
            
        }
        //pesquisaCidadesPorSiglaEstado("RN");
       
    }
    
    public void retornaFuncionario()
    {
        for(Funcionario f : funcionarioDao.findFuncionarioEntities())
        {
            if(f.getCpf().equals(cpf))
            {
                gravarFuncionario = f;
                os.setCodigofunc(gravarFuncionario);
            }
            
        }
       
    }
    
    public OsMB() {
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
        retornaCliente();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        retornaFuncionario();
    }

    public Cliente getGravarCliente() {
        return gravarCliente;
    }

    public void setGravarCliente(Cliente gravarCliente) {
        this.gravarCliente = gravarCliente;
    }

    public Funcionario getGravarFuncionario() {
        return gravarFuncionario;
    }

    public void setGravarFuncionario(Funcionario gravarFuncionario) {
        this.gravarFuncionario = gravarFuncionario;
    }
    
    public List<Funcionario> getFuncionarios() {
        return funcionarios = funcionarioDao.findFuncionarioEntities();
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public List<Cliente> getClientes() {
        return clientes = clienteDao.findClienteEntities();
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }
    
    
    public void cadastraOs() {
        osDao.create(os);
    }

    public void alterarOs() {
        try {
            osDao.edit(os);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pesquisarOs() {
        osDao.findOsEntities();
    }
    public List<Cliente> listarCliente()
    {
        return clienteDao.findClienteEntities();
       
    }

    public void excluirOs(Integer codigo) throws IllegalOrphanException {
        try {
            osDao.destroy(codigo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
