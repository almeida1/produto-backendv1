package com.fatec.sigvs.model;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true) // nao funciona com @Valid tem que tratar na camada de persistencia
	private String cpf;
	private String nome;
	private String email;
	private String cep;
	private String dataCadastro;
	private String dataRevisao;
	private String endereco;
	public Cliente() {
	}
	public Cliente(String cpf, String nome,String email, String cep) {
		this.cpf = cpf;
		this.nome = nome;
		this.email = email;
		this.cep = cep;
		DateTime dataAtual = new DateTime();
		setDataCadastro(dataAtual);

	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public void setDataCadastro(DateTime dataAtual) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		this.dataCadastro = dataAtual.toString(fmt);
		setDataRevisao();
	}
	public String getDataCadastro() {
		return this.dataCadastro;
	}
	public void setDataRevisao() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		DateTime data = fmt.parseDateTime(getDataCadastro());
		this.dataRevisao = data.plusDays(360).toString(fmt);
	}
	public String getDataRevisao() {
		return this.dataRevisao;
	}
	public Integer verificaRevisao() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd");
		DateTime dataAtual = fmt.parseDateTime(new DateTime().toString(fmt));
		DateTime dataDevolucaoPrevista = fmt.parseDateTime(getDataRevisao());
		int dias = Days.daysBetween(dataAtual, dataDevolucaoPrevista).getDays();
		return dias;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", cpf=" + cpf + ", nome=" + nome + ", email=" + email + ", cep=" + cep
				+ ", dataCadastro=" + dataCadastro + ", dataRevisao=" + dataRevisao + ", endereco=" + endereco + "]";
	}
		
}
