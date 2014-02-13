/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ProdutoServicoJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.ProdutoServico;
import util.EMF;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class ProdutoServicoMB {

    private ProdutoServico produtoServico = new ProdutoServico();
    private List<ProdutoServico> produtoServicos = new ArrayList<ProdutoServico>();

   
    private ProdutoServicoJpaController produtoServicoDao = new ProdutoServicoJpaController(EMF.getEntityManagerFactory());

    public ProdutoServicoMB() {
        //setTipoDOServico(codigo);
    }

    public List<ProdutoServico> getProdutoServicos() {
        return produtoServicos = produtoServicoDao.findProdutoServicoEntities();
    }

    public void setProdutoServicos(List<ProdutoServico> produtoServicos) {
        this.produtoServicos = produtoServicos;
    }
    
    public void setTipoDOServico(int cod)
    {
        if(produtoServico.getCodigo().equals(1))
            produtoServico.setTipo("Produto");
        else if(produtoServico.getCodigo().equals(2))
            produtoServico.setTipo("Servico");
        else
            produtoServico.setTipo(null);
    }

    
    public void cadastraProdutoServico() {
        try {
            produtoServicoDao.create(produtoServico);
        } catch (Exception ex) {
            Logger.getLogger(ProdutoServicoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alterarProdutoServico() {
        try {
            produtoServicoDao.edit(produtoServico);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pesquisarProdutoServico() {
         produtoServicoDao.findProdutoServicoEntities();
    }

    public void excluirProdutoServico(Integer codigo) throws IllegalOrphanException {
        try {
            produtoServicoDao.destroy(codigo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public ProdutoServico getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

}
