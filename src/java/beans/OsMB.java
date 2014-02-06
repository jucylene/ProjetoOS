/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.OsJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
    private OsJpaController osDao = new OsJpaController(EMF.getEntityManagerFactory());
    public OsMB() {
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

    public void excluirOs(Integer codigo) throws IllegalOrphanException {
        try {
            osDao.destroy(codigo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
