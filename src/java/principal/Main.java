/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import beans.ClienteMB;
import modelo.Cliente;

/**
 *
 * @author Helismara
 */
public class Main {
    public static void main(String[] args)
    {
        Cliente cliente2 = new Cliente();
        cliente2.setNome("Joao");
        cliente2.setBairro("penedo");
        cliente2.setCelular("9999-9999");
        cliente2.setCidade("caico");
        cliente2.setEmail("jose_@hotmail.com");
        cliente2.setCpfoucnpj("1111111111111111");
        cliente2.setEstado("RN");
        cliente2.setPontodereferencia("algum");
        cliente2.setNumero("1");
        cliente2.setCidade("caico");
        cliente2.setRua("rua qualquer");
        cliente2.setSenha("fkjhsdklf");
        
        ClienteMB dao = new ClienteMB();
        dao.setCliente(cliente2);
        dao.cadastraCliente();
        
        
        
    }    
}
