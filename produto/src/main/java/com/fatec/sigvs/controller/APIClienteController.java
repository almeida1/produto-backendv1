package com.fatec.sigvs.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.sigvs.model.Cliente;
import com.fatec.sigvs.service.ClienteServico;

@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/clientes")
public class APIClienteController {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	ClienteServico clienteServico;

	@PostMapping
	public ResponseEntity<Object> cadastraProduto(@RequestBody Cliente c) {
		logger.info(">>>>>> apicliente controller cadastrar cliente iniciado...");
		Optional<Cliente> cliente = clienteServico.cadastrar(c);
		if (cliente.isPresent())
			return ResponseEntity.status(HttpStatus.CREATED).body(cliente.get());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Informações inválidas");
	}

	@GetMapping
	public ResponseEntity<List<Cliente>> consultaTodos() {
		return ResponseEntity.status(HttpStatus.OK).body(clienteServico.consultaCliente());
	}
}
