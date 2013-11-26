package br.ufjf.tcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * DTO da Tabela {@code Curso} contém os atributos e relacionamentos da mesma.
 * 
 */
@Entity
@Table(name = "Departamento")
public class Departamento implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Campo com ID do Departamento. Relaciona com a coluna {@code idDepartamento} do banco e
	 * é gerado por autoincrement do MySQL através das anotações
	 * {@code @GeneratedValue(generator = "increment")} e
	 * {@code @GenericGenerator(name = "increment", strategy = "increment")}
	 * 
	 */
	@Id
	@Column(name = "idDepartamento", unique = true, nullable = false)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int idDepartamento;

	/**
	 * Campo com o nome do curso. Relaciona com a coluna {@code codigoDepartamento} do
	 * banco através da anotação
	 * {@code @Column(name = "codigoDepartamento", length = 80, nullable = false)}.
	 */
	@Column(name = "codigoDepartamento", length = 50, nullable = false)
	private String codigoDepartamento;
	
	/**
	 * Campo com o nome do curso. Relaciona com a coluna {@code nomeDepartamento} do
	 * banco através da anotação
	 * {@code @Column(name = "nomeDepartamento", length = 80, nullable = false)}.
	 */
	@Column(name = "nomeDepartamento", length = 80, nullable = false)
	private String nomeDepartamento;

	/**
	 * Relacionamento 1 para N entre Departamento e Usuários. Mapeada em
	 * {@link Usuario} pela variável {@code departamento} e retorno do tipo
	 * {@code LAZY} que indica que não será carregado automáticamente este dado
	 * quando retornarmos o {@link Departamento}.
	 * 
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento")
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	public int getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(int idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getNomeDepartamento() {
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
