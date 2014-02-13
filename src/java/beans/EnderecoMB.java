package beans;

import dao.CidadeJpaController;
import dao.EstadoJpaController;
import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import modelo.Cidade;
import modelo.Estado;
import util.EMF;


/**
 * CidadeUF Combo Controller
 *
 * @author César Barbosa
 */
@ManagedBean
@ApplicationScoped
public class EnderecoMB implements Serializable{
   
  
    private CidadeJpaController cidDao = new CidadeJpaController(EMF.getEntityManagerFactory());
    private EstadoJpaController estDao;
    private String estado;
    private String cidade;
    private SortedMap<String,String> estados = new TreeMap<String, String>();
    private SortedMap<String,SortedMap<String,String>> cidadesData = new TreeMap<String, SortedMap<String,String>>();
    private SortedMap<String,String> cidades = new TreeMap<String, String>(); 
   
    
    /*
     * Construtor
     */
    public EnderecoMB() { 
        cidDao = new CidadeJpaController(EMF.getEntityManagerFactory());
        estDao = new EstadoJpaController(EMF.getEntityManagerFactory());
        PovoarListaDeEstados();
        //PovoarListaDeCidades(estado);
    }
    

    public CidadeJpaController getCidDao() {
        return cidDao;
    }

    public void setCidDao(CidadeJpaController cidDao) {
        this.cidDao = cidDao;
    }

    public EstadoJpaController getEstDao() {
        return estDao;
    }

    public void setEstDao(EstadoJpaController estDao) {
        this.estDao = estDao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    
    
    
   
    /*
     * Propriedades
     */
    
     
    
   
    public SortedMap<String, String> getEstados() {  
        return estados;
        
    }  
    public void setEstados(SortedMap<String, String> ufs) {  
        this.estados = estados;  
    }  
 
    
   
    public SortedMap<String, SortedMap<String, String>> getCidadesData() {  
        return cidadesData;  
    }  
    public void setCidadesData(SortedMap<String, SortedMap<String, String>> cidadesData) {  
        this.cidadesData = cidadesData;  
    }  
     
     
   
    public SortedMap<String, String> getCidades() {  
        return cidades;  
    }  
    public void setCidades(SortedMap<String, String> cidades) {  
        this.cidades = cidades;  
    }  
 

    /*
     * Helpers
     */
    private void PovoarListaDeEstados(){
        System.out.println("Populando Estados...");
        List<Estado> enderecoEstados = estDao.findEstadoEntities();
        for (Estado estado : enderecoEstados) {
            estados.put(estado.getSigla(), estado.getSigla());
           
        }
        System.out.println("Populando Estados com "+estados.size()+" itens...");
    }
   
    /*
    public void PovoarListaDeCidades(String nomeEstado){
        List<Cidade> listaDeCidades = cidDao.pesquisarPorCidadePelaSigla(nomeEstado);
        for (Cidade cidade : listaDeCidades) {
            cidades.put(cidade.getNome(), cidade.getNome());
           
        }
        
    }
   
   * 
   
    /*
     * Action handlers
     */
    public void EscolhaDoEstado() {  
        if(estado !=null && !estado.equals(""))  
            cidades = cidadesData.get(estado);  
        else  
            cidades = new TreeMap<String, String>();  
    }  

}
