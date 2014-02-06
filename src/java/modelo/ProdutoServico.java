/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Helismara
 */
@Entity
@Table(name = "produto__servico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProdutoServico.findAll", query = "SELECT p FROM ProdutoServico p"),
    @NamedQuery(name = "ProdutoServico.findByCodigo", query = "SELECT p FROM ProdutoServico p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "ProdutoServico.findByNome", query = "SELECT p FROM ProdutoServico p WHERE p.nome = :nome"),
    @NamedQuery(name = "ProdutoServico.findByTipo", query = "SELECT p FROM ProdutoServico p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "ProdutoServico.findByPreco", query = "SELECT p FROM ProdutoServico p WHERE p.preco = :preco"),
    @NamedQuery(name = "ProdutoServico.findByQuantidade", query = "SELECT p FROM ProdutoServico p WHERE p.quantidade = :quantidade")})
public class ProdutoServico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "preco")
    private BigDecimal preco;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoProdutoServico")
    private Collection<Item> itemCollection;

    public ProdutoServico() {
    }

    public ProdutoServico(Integer codigo) {
        this.codigo = codigo;
    }

    public ProdutoServico(Integer codigo, String nome, String tipo, BigDecimal preco, int quantidade) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @XmlTransient
    public Collection<Item> getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(Collection<Item> itemCollection) {
        this.itemCollection = itemCollection;
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
        if (!(object instanceof ProdutoServico)) {
            return false;
        }
        ProdutoServico other = (ProdutoServico) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.ProdutoServico[ codigo=" + codigo + " ]";
    }
    
}
