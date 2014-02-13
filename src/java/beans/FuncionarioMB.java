/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CidadeJpaController;
import dao.EstadoJpaController;
import dao.FuncionarioJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Cidade;
import modelo.Cliente;
import modelo.Endereco;
import modelo.Estado;
import modelo.Funcionario;
import util.EMF;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class FuncionarioMB {

    private Funcionario funcionario = new Funcionario();
    private FuncionarioJpaController funcionarioDao = new FuncionarioJpaController(EMF.getEntityManagerFactory());
    private EstadoJpaController estadoDao = new EstadoJpaController(EMF.getEntityManagerFactory());
    private CidadeJpaController cidadeDao = new CidadeJpaController(EMF.getEntityManagerFactory());
    private Estado estado = new Estado();
    private Cidade cidade = new Cidade();
    private Endereco endereco = new Endereco();
    private List<Estado> estados = new ArrayList<Estado>();
    private List<Cidade> cidades = new ArrayList<Cidade>();
    private List<Cidade> cidadePorEstado = new ArrayList<Cidade>();
    private String estSig;
    
    private Integer codCidade;
    
    private Estado gravarEstado;
    private Cidade gravarCidade;
    private List<Cliente> clientes;

    public FuncionarioMB() {
    }

    public void cadastraFuncionario() throws PreexistingEntityException, Exception {
        funcionarioDao.create(funcionario);
    }

    public void alterarFuncionario() {
        try {
            funcionarioDao.edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pesquisarFuncionario() {
        funcionarioDao.findFuncionarioEntities();
    }

    public void excluirFuncionario(String codigo) {
        try {
            funcionarioDao.destroy(codigo);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void retornaEstado()
    {
        for(Estado est : estadoDao.findEstadoEntities())
        {
            if(est.getSigla().equals(estSig))
            {
                gravarEstado = est;
                endereco.setSiglaestado(gravarEstado);
            }
            
        }
        //pesquisaCidadesPorSiglaEstado("RN");
       
    }
    
    public void retornaCidade()
    {
        for(Cidade cd : cidadeDao.findCidadeEntities())
        {
            if(cd.getCodigo().equals(codCidade))
            {
                gravarCidade = cd;
                endereco.setCodigocidade(gravarCidade);
            }
            
        }
       
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
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

    public List<Cidade> getCidadePorEstado() {
        return cidadePorEstado;
    }

    public void setCidadePorEstado(List<Cidade> cidadePorEstado) {
        this.cidadePorEstado = cidadePorEstado;
    }

    public String getEstSig() {
        return estSig;
    }

    public void setEstSig(String estSig) {
        this.estSig = estSig;
        retornaEstado();
    }

    public Integer getCodCidade() {
        return codCidade;
    }

    public void setCodCidade(Integer codCidade) {
        this.codCidade = codCidade;
        retornaCidade();
    }

    public Estado getGravarEstado() {
        return gravarEstado;
    }

    public void setGravarEstado(Estado gravarEstado) {
        this.gravarEstado = gravarEstado;
    }

    public Cidade getGravarCidade() {
        return gravarCidade;
    }

    public void setGravarCidade(Cidade gravarCidade) {
        this.gravarCidade = gravarCidade;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    

}
