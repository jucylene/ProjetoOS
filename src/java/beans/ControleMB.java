/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Helismara
 */
@ManagedBean
@RequestScoped
public class ControleMB {

    /**
     * Creates a new instance of ControleMB
     */
    private ClienteMB clienteMB;
    private FuncionarioMB funcionarioMB;
    private EnderecoMB enderecoMB;
    private OsMB osMB;
    private ItemMB itemMB;
    private ProdutoServicoMB produtoServicoMB;
    
    
    
    
    public ControleMB() {
    }

    
    
    public ClienteMB getClienteMB() {
        if(clienteMB == null)
            return new ClienteMB();
        else
            return this.getClienteMB();
    }
    
    public FuncionarioMB getFuncionarioMB() {
        if(funcionarioMB == null)
            return new FuncionarioMB();
        else
            return this.getFuncionarioMB();
    }
    
    
    
    
     
   

    
}
