/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import modelo.Cliente;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Helismara
 */
public class ClienteMBTest {
    
    public ClienteMBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of cadastraCliente method, of class ClienteMB.
     */
    @Test
    public void testCadastraCliente() {
        System.out.println("cadastraCliente");
        ClienteMB instance = new ClienteMB();
        instance.cadastraCliente();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of alterarCliente method, of class ClienteMB.
     */
    @Test
    public void testAlterarCliente() {
        System.out.println("alterarCliente");
        ClienteMB instance = new ClienteMB();
        instance.alterarCliente();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pesquisarCliente method, of class ClienteMB.
     */
    @Test
    public void testPesquisarCliente() {
        System.out.println("pesquisarCliente");
        ClienteMB instance = new ClienteMB();
        instance.pesquisarCliente();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of excluirCliente method, of class ClienteMB.
     */
    @Test
    public void testExcluirCliente() {
        System.out.println("excluirCliente");
        Integer codigo = null;
        ClienteMB instance = new ClienteMB();
        instance.excluirCliente(codigo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCliente method, of class ClienteMB.
     */
    @Test
    public void testGetCliente() {
        System.out.println("getCliente");
        ClienteMB instance = new ClienteMB();
        Cliente expResult = null;
        Cliente result = instance.getCliente();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCliente method, of class ClienteMB.
     */
    @Test
    public void testSetCliente() {
        System.out.println("setCliente");
        Cliente cliente = null;
        ClienteMB instance = new ClienteMB();
        instance.setCliente(cliente);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}