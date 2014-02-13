/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.ItemJpaController;
import dao.OsJpaController;
import dao.ProdutoServicoJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Item;
import modelo.Os;
import modelo.ProdutoServico;
import util.EMF;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class ItemMB {

    /**
     * Creates a new instance of ItemMB
     */
    public ItemMB() {
    }
    private Item item = new Item();
    private ProdutoServico produtoServico = new ProdutoServico();
    private Os os = new Os();
    private List<ProdutoServico> produtosServicos = new ArrayList<ProdutoServico>();
    private List<Item> itens = new ArrayList<Item>();
    private List<Os> oss = new ArrayList<Os>();
    private ItemJpaController itemDao = new ItemJpaController(EMF.getEntityManagerFactory());
    private OsJpaController osDao = new OsJpaController(EMF.getEntityManagerFactory());
    private ProdutoServicoJpaController prodServDao = new ProdutoServicoJpaController(EMF.getEntityManagerFactory());
    public double resultado = 0.0;
    private double resuladoSubTotal = 0.0;
    private double resuladoTotal = 0.0;

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getResuladoSubTotal() {
        return resuladoSubTotal;
    }

    public void setResuladoSubTotal(double resuladoSubTotal) {
        this.resuladoSubTotal = resuladoSubTotal;
    }

    public double getResuladoTotal() {
        return resuladoTotal;
    }

    public void setResuladoTotal(double resuladoTotal) {
        this.resuladoTotal = resuladoTotal;
    }
    
    
    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
    
    
    public ItemJpaController getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemJpaController itemDao) {
        this.itemDao = itemDao;
    }

    public List<ProdutoServico> getProdutosServicos() {
        return produtosServicos = prodServDao.findProdutoServicoEntities();
    }

    public void setProdutosServicos(List<ProdutoServico> produtosServicos) {
        this.produtosServicos = produtosServicos;
    }

    public List<Os> getOss() {
        return oss = osDao.findOsEntities();
    }

    public void setOss(List<Os> oss) {
        this.oss = oss;
    }
    
    public ProdutoServico getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }
    

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    

    

    public void cadastraItem() {
        itemDao.create(item);
    }

    public void alterarItem() {
        try {
            itemDao.edit(item);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pesquisarItem() {
        itemDao.findItemEntities();
    }

    public void excluirItem(Integer codigo) {
        try {
            itemDao.destroy(codigo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double subTotal()
    {
        
        int quant = item.getQuantidade();
        double preco = produtoServico.getPreco().doubleValue();
        
        resultado = quant * preco;
        
        return resultado;
        
    }
    
    public double total()
    {
        Double total = 0.0;
        for(Item i : itens)
        {
            total += resultado;
        }
        return 0.0;
    }
    
    public void calcularSubTotal()
    {
        resuladoSubTotal = subTotal();
    }
}
