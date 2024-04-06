package com.fatec.sigvs.ti;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.sigvs.model.Catalogo;
import com.fatec.sigvs.model.IImagemRepository;
import com.fatec.sigvs.model.IProdutoRepository;
import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IProdutoServico;

@SpringBootTest
public class MantemProdutoTest {
	@Autowired
	private IProdutoServico produtoServico;
	@Autowired
	IImagemRepository imagemRepository;
	@Autowired
	IProdutoRepository produtoRepository;
	private Long id = null;// armazena o ultimo id gerado no banco

	@BeforeEach
	public void setup() {

		byte[] arquivo1 = null;
		Produto produto1 = new Produto("Eletrobomba para maquina de lavar", "maquina de lavar", 51.66, 12);
		produtoRepository.save(produto1);
		Path path = Paths.get("c:/temp/produto1.jpg");
		try {
			InputStream file = Files.newInputStream(path);
			arquivo1 = file.readAllBytes();
		} catch (Exception e) {
			System.out.println(">>>>>> erro no acesso ao arquivo");
			fail("erro");
		}
		Imagem imagem = new Imagem();
		imagem.setId(produto1.getId()); // associa o id do produto ao id da imagem
		imagem.setNome("produto1.jpg");
		imagem.setCaminho("imagens/" + imagem.getNome());
		imagem.setArquivo(arquivo1);

		imagemRepository.save(imagem);
		id = produto1.getId();
		

	}

	@Test
	public void ct01_quando_repositorio_vazio_consulta_retorna_vazio() {
		// dado que não existem registros no db
		produtoRepository.deleteAll();
		imagemRepository.deleteAll();
		// quando consulto o catalogo
		List<Catalogo> catalogo = produtoServico.consultaCatalogo();
		// entao o catalogo esta vazio
		assertTrue(catalogo.isEmpty());
	}

	@Test
	public void ct02_quando_existem_itens_no_catalogo_consulta_retornar_vazio_false() {
		// dado que existem produtos cadastrados
		// quando conulto o catalogo
		List<Catalogo> catalogo = produtoServico.consultaCatalogo();
		// entao o catalogo nao esta vazio
		assertFalse(catalogo.isEmpty());
	}

	@Test
	public void ct03_consulta_descricao_vazia_retorna_vazio() {
		// dado que existem produtos cadastrados
		List<Catalogo> catalogo = produtoServico.consultaPorDescricao("");
		assertEquals(0, catalogo.size());
		assertTrue(catalogo.isEmpty());
	}

	@Test
	public void ct04_quando_consulta_parcial_retorna_true() {
		// dado que existem produtos cadastrados
		List<Catalogo> catalogo1 = produtoServico.consultaPorDescricao("Eletrobomba");
		assertFalse(catalogo1.isEmpty());
	}

	@Test
	public void ct05_quando_consulta_descricao_invalida_retorna_vazio() {
		// dado que existem produtos cadastrados
		List<Catalogo> catalogo = produtoServico.consultaPorDescricao("Descrição inexistente");
		assertTrue(catalogo.isEmpty());
	}

	@Test
	public void ct06_quando_produto_valido_cadastra_com_sucesso() {
		// dado que as informacoes do produto sao validas
		Produto produto2 = new Produto("Eletrobomba para maquina de lavar", "maquina de lavar", 51.66, 12);
		// quando cadastro o produto
		Optional<Produto> resultado = produtoServico.cadastrar(produto2);
		// retorna o produto e o id
		assertTrue(resultado.isPresent());
		assertEquals(id + 1, resultado.get().getId());
	}

	@Test
	public void ct07_quando_produto_invalido_retorna_erro() {
		Produto produto = null;
		try {
			produto = new Produto("", "maquina de lavar", 51.66, 12);
			fail("deveria falhar descricao em branco");
		} catch (IllegalArgumentException e) {
			assertEquals("A descricao não deve estar em branco", e.getMessage());
			assertNull(produto);
		}
	}

	@Test
	public void ct08_quando_id_valido_na_consulta_retorna_detalhes_do_produto() {

		String idExistente = id.toString();
		Optional<Produto> resultado = produtoServico.consultarPorId(idExistente);
		assertTrue(resultado.isPresent());
	}

	@Test
	public void ct09_quando_id_inexistente_na_consulta_retorna_erro() {
		String idInexistente = "999";
		Optional<Produto> resultado = produtoServico.consultarPorId(idInexistente);
		assertFalse(resultado.isPresent());
	}

	@Test
	public void ct10_quando_informacoes_do_produto_sao_validas_aturaliza_com_sucesso() {

		Produto produtoAtualizado = new Produto("novo produto", "maquina de lavar", 51.66, 12);
		produtoAtualizado.setId(id);
		Optional<Produto> resultado = produtoServico.atualizar(id, produtoAtualizado);
		assertTrue(resultado.isPresent());
		List<Catalogo> catalogo1 = produtoServico.consultaPorDescricao("novo");
		assertFalse(catalogo1.isEmpty());
	}

	@Test
	public void ct11_quando_id_valido_exclui_com_sucesso() {

		String idExistente = id.toString();
		assertDoesNotThrow(() -> produtoServico.excluir(idExistente));
		String i = id.toString();
		Optional<Produto> resultado = produtoServico.consultarPorId(i);
		assertFalse(resultado.isPresent());
	}

	@Test
	public void ct12_quando_id_invalido_retorna_erro() {
		String idInexistente = "999";
		assertDoesNotThrow(() -> produtoServico.excluir(idInexistente));
	}
}
