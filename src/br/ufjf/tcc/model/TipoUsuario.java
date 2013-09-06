package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * DTO da Tabela {@code TipoUsuario} contém os atributos e relacionamentos da
 * mesma.
 * 
 */
@Entity
@Table(name = "TipoUsuario")
public class TipoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID da Tipo de Usuario. Relaciona com a coluna
	 * {@code idTipoUsuario} do banco e é gerado por autoincrement do MySQL
	 * através das anotações {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idTipoUsuario", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idTipoUsuario;

	/**
	 * Campo com nome do tipo de usuario. Relaciona com a coluna
	 * {@code nomeTipoUsuario} do banco através da anotação
	 * {@code @Column(name = "nomeTipoUsuario", length = 45, nullable = false)}.
	 */
	@Column(name = "nomeTipoUsuario", length = 45, nullable = false)
	private String nomeTipoUsuario;

	/**
	 * Relacionamento N para N entre TipoUsuario e Permissoes. Mapeando
	 * {@link Permissoes} na variável {@code permissoes} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link TipoUsuario}.
	 * 
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TipoUsuario_Permissoes", joinColumns = { @JoinColumn(name = "idTipoUsuario", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "idPermissao", nullable = false, updatable = false) })
	private List<Permissoes> permissoes = new ArrayList<Permissoes>();

	/**
	 * Relacionamento 1 para N entre TipoUsuario e Usuario. Mapeada em
	 * {@link Usuario} pela variável {@code tipoUsuario} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link TipoUsuario} .
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoUsuario")
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	public int getIdTipoUsuario() {
		return idTipoUsuario;
	}

	public void setIdTipoUsuario(int idTipoUsuario) {
		this.idTipoUsuario = idTipoUsuario;
	}

	public String getNomeTipoUsuario() {
		return nomeTipoUsuario;
	}

	public void setNomeTipoUsuario(String nomeTipoUsuario) {
		this.nomeTipoUsuario = nomeTipoUsuario;
	}

	public List<Permissoes> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissoes> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
