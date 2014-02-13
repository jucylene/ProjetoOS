/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Helismara
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findByCodigo", query = "SELECT i FROM Item i WHERE i.codigo = :codigo"),
    @NamedQuery(name = "Item.findByCodigoProdutoServico", query = "SELECT i FROM Item i WHERE i.codigoProdutoServico = :codigoProdutoServico"),
    @NamedQuery(name = "Item.findByProtocoloOs", query = "SELECT i FROM Item i WHERE i.protocoloOs = :protocoloOs"),
    @NamedQuery(name = "Item.findByQuantidade", query = "SELECT i FROM Item i WHERE i.quantidade = :quantidade"),
    @NamedQuery(name = "Item.findByValor", query = "SELECT i FROM Item i WHERE i.valor = :valor")})
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "codigo_produto_servico")
    private int codigoProdutoServico;
    @Basic(optional = false)
    @Column(name = "protocolo_os")
    private int protocoloOs;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;

    public Item() {
    }

    public Item(Integer codigo) {
        this.codigo = codigo;
    }

    public Item(Integer codigo, int codigoProdutoServico, int protocoloOs, int quantidade, BigDecimal valor) {
        this.codigo = codigo;
        this.codigoProdutoServico = codigoProdutoServico;
        this.protocoloOs = protocoloOs;
        this.quantidade = quantidade;
        this.valor = valor;
    }

        
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getCodigoProdutoServico() {
        return codigoProdutoServico;
    }

    public void setCodigoProdutoServico(int codigoProdutoServico) {
        this.codigoProdutoServico = codigoProdutoServico;
    }

    public int getProtocoloOs() {
        return protocoloOs;
    }

    public void setProtocoloOs(int protocoloOs) {
        this.protocoloOs = protocoloOs;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Item[ codigo=" + codigo + " ]";
    }
    
}
