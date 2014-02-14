


package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import static util.FacesUtil.adicionarMensagem;


public class FacesUtil {
   
    /**
     * Adiciona uma mensagem ao FacesContext para o id especificado.
     * @param id o identificador da tag que receberá a mensagem. Exemplo: "formCadastro:nome"
     * @param mensagem a mensagem a ser adicionada.
     */
    public static void adicionarMensagem(String id, String mensagem){
        FacesMessage message = new FacesMessage(mensagem);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(id, message);
    }
    
    /**
     * Adiciona uma mensagem ao FacesContext.
     * @param mensagem a mensagem a ser adicionada.
     */
    public static void adicionarMensagem(String mensagem){
        adicionarMensagem(null, mensagem);
    }
    
   
    
   
}