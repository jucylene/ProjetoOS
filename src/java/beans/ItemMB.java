/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.ItemJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Item;
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
    private ItemJpaController itemDao = new ItemJpaController(EMF.getEntityManagerFactory());

    

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
    
}
