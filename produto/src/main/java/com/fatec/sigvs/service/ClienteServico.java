package com.fatec.sigvs.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fatec.sigvs.model.Cliente;
import com.fatec.sigvs.model.Endereco;
import com.fatec.sigvs.model.IClienteRepository;

@Service
public class ClienteServico implements IClienteServico{
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	IClienteRepository repository;

	@Override
	public List<Cliente> consultaCliente() {
		List<Cliente> listaDeClientes = repository.findAll();
		return listaDeClientes;
	}

	@Override
	public List<Cliente> consultaPorNome(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Cliente> cadastrar(Cliente cliente) {
		Cliente umCliente=null;
		try {
			String endereco = obtemEndereco(cliente.getCep());
			if (endereco != "") {
				logger.info(">>>>>> cliente servico cadastrar ...  ");
				cliente.setDataCadastro(new DateTime());
				cliente.setEndereco(endereco);
				umCliente = repository.save(cliente);
			}
		} catch (Exception e) { // captura validacoes na camada de persistencia
			
			if (e.getMessage().contains("could not execute statement")) {
				logger.info(">>>>>> 2. cliente ja cadastrado ==> " + e.getMessage());
				
			} else {
				logger.error(">>>>>> 2. erro nao esperado ==> " + e.getMessage());
				
			}
			return Optional.ofNullable(umCliente);
		}
		return Optional.ofNullable(umCliente);
	}

	@Override
	public Optional<Cliente> consultarPorId(String id) {
		logger.info(">>>>>> servico cliente consulta por id chamado");
		long codCliente = Long.parseLong(id);
		return repository.findById(codCliente);
	}


	@Override
	public Optional<Cliente> atualizar(Long id, Cliente clienteAtualizado) {
		logger.info(">>>>>> servico atualizar informacoes de cliente chamado para id =>" + id);
		Cliente cliente = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cliente nao cadastrado"));
		cliente.setCpf(clienteAtualizado.getCpf());
		cliente.setNome(clienteAtualizado.getNome());
		cliente.setEmail(clienteAtualizado.getEmail());
		String endereco = obtemEndereco(clienteAtualizado.getCep());
		cliente.setCep(clienteAtualizado.getCep());
		cliente.setEndereco(endereco);
		return Optional.ofNullable(repository.save(cliente));
	}


	@Override
	public void excluir(String id) {
		// TODO Auto-generated method stub
		
	}
	public String obtemEndereco(String cep) {
		RestTemplate template = new RestTemplate();
		String url = "https://viacep.com.br/ws/{cep}/json/";
		Endereco endereco = template.getForObject(url, Endereco.class, cep);
		logger.info(">>>>>> obtem endereco ==> " + endereco.toString());
		return endereco.getLogradouro();
	}
}
