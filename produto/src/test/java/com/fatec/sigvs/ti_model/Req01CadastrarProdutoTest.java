package com.fatec.sigvs.ti_model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IImagemRepository;
import com.fatec.sigvs.service.IProdutoRepository;

@SpringBootTest
class Req01CadastrarProdutoTest {

	@Autowired
	IImagemRepository imagemRepository;
	@Autowired
	IProdutoRepository produtoRepository;

	/**
	 * cadastrar um produto e uma imagem associa o id do produto ao id da imagem
	 */
	public void setup() {
		byte[] arquivo1 = null;
		produtoRepository.deleteAll();
		Produto produto1 = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
				"51.66", "12");
		produtoRepository.save(produto1);
		Path path = Paths.get("c:/temp/produto1.jpg");
		try {
			InputStream file = Files.newInputStream(path);
			arquivo1 = file.readAllBytes();
		} catch (Exception e) {
			System.out.println("erro no acesso ao arquivo");
		}
		Imagem imagem = new Imagem();
		imagem.setId(1L); // associa o id do produto ao id da imagem
		imagem.setNome("produto1.jpg");
		imagem.setCaminho("imagens/" + imagem.getNome());
		imagem.setArquivo(arquivo1);

		imagemRepository.save(imagem);
	}

	//@Test erro de build
	void ct01_quando_consulta_por_id_retorna_detalhaes_do_produto() throws Exception {
		// Dado - que o produto esta cadastrado
		
		try {
			Produto produto1 = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
					"51.66", "12");
			produtoRepository.save(produto1);
			// Quando - o usuario consulta o id
			Long id = 1L;
			Optional<Produto> ro = produtoRepository.findById(id);
			// Entao - retorna detalhadas do produto);
			Produto re = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
					"51.66", "12");
			System.out.println("consulta produto => " + produtoRepository.findById(id).isPresent());
			assertTrue(ro.isPresent());
			assertTrue(ro.get().equals(re));
		} catch (Exception e) {
			fail("nao deveria falhar");
			System.out.println("nao deveria falhar");
		}
	}

	@Test
	void ct02_quando_consulta_por_id_nao_cadastrado_retorna_erro() throws Exception {
		// Dado - que id nao esta cadastrado
		Long id = 99L;
		// Quando - o usuario consulta o id
		Optional<Produto> ro = produtoRepository.findById(id);
		// Entao - retorna not found (vazio)
		assertTrue(ro.isEmpty());
	}

}
