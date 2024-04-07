package com.fatec.sigvs.service;

import java.util.List;
import java.util.Optional;

import com.fatec.sigvs.model.Cliente;


public interface IClienteServico {
	public List<Cliente> consultaCliente();
	
	public List<Cliente> consultaPorNome(String nome);
	public Optional <Cliente> cadastrar(Cliente cliente);
	public Optional <Cliente> consultarPorId(String id);
	public Optional <Cliente> atualizar(Long id, Cliente cliente);
	public void excluir(String id);
}
