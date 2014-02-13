
import dao.CidadeJpaController;
import dao.EnderecoJpaController;
import dao.EstadoJpaController;
import dao.exceptions.PreexistingEntityException;
import modelo.Cidade;
import modelo.Endereco;
import modelo.Estado;
import util.EMF;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Helismara
 */
public class Main {

    private Endereco end = new Endereco();
    private Cidade cid = new Cidade();
    private Estado est = new Estado();
    private EstadoJpaController estadoDao = new EstadoJpaController(EMF.getEntityManagerFactory());
    private CidadeJpaController cidadeDao = new CidadeJpaController(EMF.getEntityManagerFactory());
    private EnderecoJpaController enderecoDao = new EnderecoJpaController(EMF.getEntityManagerFactory());
    public void main(String[] args) throws PreexistingEntityException, Exception {
        // TODO code application logic here
        
         est.setSigla("MONNA");
         est.setNome("Monnalisa");
         est.setPopulacao(1);
         est.setRegiao("Qualquer");
         estadoDao.create(est);
    }
}
