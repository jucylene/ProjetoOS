/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cidade.findAll", query = "SELECT c FROM Cidade c"),
    @NamedQuery(name = "Cidade.findByCodigo", query = "SELECT c FROM Cidade c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Cidade.findByNome", query = "SELECT c FROM Cidade c WHERE c.nome = :nome"),
    @NamedQuery(name = "Cidade.findByNomeCompleto", query = "SELECT c FROM Cidade c WHERE c.nomeCompleto = :nomeCompleto"),
    @NamedQuery(name = "Cidade.findByCep", query = "SELECT c FROM Cidade c WHERE c.cep = :cep"),
    @NamedQuery(name = "Cidade.findByCodigoMunicipio", query = "SELECT c FROM Cidade c WHERE c.codigoMunicipio = :codigoMunicipio")})
public class Cidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "nome_completo")
    private String nomeCompleto;
    @Column(name = "cep")
    private String cep;
    @Column(name = "codigo_municipio")
    private Long codigoMunicipio;
    @JoinColumn(name = "sigla_estado", referencedColumnName = "sigla")
    @ManyToOne(optional = false)
    private Estado siglaEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigocidade")
    private Collection<Endereco> enderecoCollection;

    public Cidade() {
    }

    public Cidade(Integer codigo) {
        this.codigo = codigo;
    }

    public Cidade(Integer codigo, String nome, String nomeCompleto) {
        this.codigo = codigo;
        this.nome = nome;
        this.nomeCompleto = nomeCompleto;
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

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Estado getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(Estado siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    @XmlTransient
    public Collection<Endereco> getEnderecoCollection() {
        return enderecoCollection;
    }

    public void setEnderecoCollection(Collection<Endereco> enderecoCollection) {
        this.enderecoCollection = enderecoCollection;
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
        if (!(object instanceof Cidade)) {
            return false;
        }
        Cidade other = (Cidade) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Cidade[ codigo=" + codigo + " ]";
    }
    
}
