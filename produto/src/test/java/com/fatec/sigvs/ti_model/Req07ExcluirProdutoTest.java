package com.fatec.sigvs.ti_model;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IProdutoRepository;

@SpringBootTest
class Req07ExcluirProdutoTest {
	@Autowired
	IProdutoRepository produtoRepository;

	public void setup() {
		Produto produto1 = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
				"51.66", "12");
		produtoRepository.save(produto1);
	}

	@Test
	void ct01_quando_produto_cadastrado_excluir_com_sucesso() {
		produtoRepository.deleteById(1L);
	}

}
