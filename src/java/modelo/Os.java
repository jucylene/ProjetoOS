/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Helismara
 */
@Entity
@Table(name = "os")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Os.findAll", query = "SELECT o FROM Os o"),
    @NamedQuery(name = "Os.findByProtocolo", query = "SELECT o FROM Os o WHERE o.protocolo = :protocolo"),
    @NamedQuery(name = "Os.findByPrioridade", query = "SELECT o FROM Os o WHERE o.prioridade = :prioridade"),
    @NamedQuery(name = "Os.findBySituacao", query = "SELECT o FROM Os o WHERE o.situacao = :situacao"),
    @NamedQuery(name = "Os.findByDatacriacao", query = "SELECT o FROM Os o WHERE o.datacriacao = :datacriacao"),
    @NamedQuery(name = "Os.findByDatatermino", query = "SELECT o FROM Os o WHERE o.datatermino = :datatermino"),
    @NamedQuery(name = "Os.findByDescricao", query = "SELECT o FROM Os o WHERE o.descricao = :descricao"),
    @NamedQuery(name = "Os.findByObservacao", query = "SELECT o FROM Os o WHERE o.observacao = :observacao")})
public class Os implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "protocolo")
    private Integer protocolo;
    @Basic(optional = false)
    @Column(name = "prioridade")
    private String prioridade;
    @Basic(optional = false)
    @Column(name = "situacao")
    private String situacao;
    @Column(name = "datacriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datacriacao;
    @Column(name = "datatermino")
    @Temporal(TemporalType.DATE)
    private Date datatermino;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "observacao")
    private String observacao;
    @JoinColumn(name = "codigofunc", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Funcionario codigofunc;
    @JoinColumn(name = "codigocliente", referencedColumnName = "cnpj")
    @ManyToOne(optional = false)
    private Cliente codigocliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "protocoloOs")
    private Collection<Item> itemCollection;

    public Os() {
    }

    public Os(Integer protocolo) {
        this.protocolo = protocolo;
    }

    public Os(Integer protocolo, String prioridade, String situacao, String descricao) {
        this.protocolo = protocolo;
        this.prioridade = prioridade;
        this.situacao = situacao;
        this.descricao = descricao;
    }

    public Integer getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Integer protocolo) {
        this.protocolo = protocolo;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(Date datacriacao) {
        this.datacriacao = datacriacao;
    }

    public Date getDatatermino() {
        return datatermino;
    }

    public void setDatatermino(Date datatermino) {
        this.datatermino = datatermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Funcionario getCodigofunc() {
        return codigofunc;
    }

    public void setCodigofunc(Funcionario codigofunc) {
        this.codigofunc = codigofunc;
    }

    public Cliente getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(Cliente codigocliente) {
        this.codigocliente = codigocliente;
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
        hash += (protocolo != null ? protocolo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Os)) {
            return false;
        }
        Os other = (Os) object;
        if ((this.protocolo == null && other.protocolo != null) || (this.protocolo != null && !this.protocolo.equals(other.protocolo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Os[ protocolo=" + protocolo + " ]";
    }
    
}
