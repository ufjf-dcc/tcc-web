package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * DTO da Tabela {@code Permissoes} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "Permissoes")
public class Permissoes implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID das Permissoes. Relaciona com a coluna {@code idPermissao}
	 * do banco e é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idPermissao", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idPermissao;

	/**
	 * Campo com nome da permissao. Relaciona com a coluna {@code nomePermissao}
	 * do banco através da anotação
	 * {@code @Column(name = "nomePermissao", length = 45, nullable = false)}.
	 */
	@Column(name = "nomePermissao", length = 45, nullable = false)
	private String nomePermissao;

	/**
	 * Relacionamento N para N entre Permissoes e TipoUsuario. Mapeada em
	 * {@link TipoUsuario} pela variável {@code permissoes} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos as {@link Permissoes} .
	 * 
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissoes")
	private List<TipoUsuario> tipousuario = new ArrayList<TipoUsuario>();

	public int getIdPermissao() {
		return idPermissao;
	}

	public void setIdPermissao(int idPermissao) {
		this.idPermissao = idPermissao;
	}

	public String getNomePermissao() {
		return nomePermissao;
	}

	public void setNomePermissao(String nomePermissao) {
		this.nomePermissao = nomePermissao;
	}

	public List<TipoUsuario> getTipousuario() {
		return tipousuario;
	}

	public void setTipousuario(List<TipoUsuario> tipousuario) {
		this.tipousuario = tipousuario;
	}

}
