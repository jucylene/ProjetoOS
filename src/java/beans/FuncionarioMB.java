/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.FuncionarioJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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

    public FuncionarioMB() {
    }

    public void cadastraFuncionario() {
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

    public void excluirFuncionario(Integer codigo) {
        try {
            funcionarioDao.destroy(codigo);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClienteMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
