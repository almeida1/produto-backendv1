package com.fatec.sigvs.ti_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
	@Test
    void ct01CadastrarProdutoComSucesso() {
		produtoRepository.deleteAll();
        Produto produto1 = new Produto("eletrobomba 110v", "maquina de lavar", "22.30", "10");
        Produto produto2 = new Produto("Tirante Original Brastemp E Consul De 7 A 12 Kg 11cm", "lavar louça", "3.90", "20");
        Produto produto3 = new Produto("Termoatuador Lavadora Colormaq Electrolux GE", "maquina de lavar", "29.70", "40");
        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3));
        assertEquals(3, produtoRepository.count());
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
	
    @Test
    void ct02_cadastrar_produto_descricao_invalida() {
        Produto produto1 = null;
        try {
            produto1 = new Produto("", "maquina de lavar", "22.30", "10");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("A descrição não deve estar em branco", e.getMessage());
            assertNull(produto1);
        }
    }
    @Test
    void ct03_cadastrar_produto_custo_invalido() {
        Produto produto1 = null;
        try {
            produto1 = new Produto("eletrobomba 110v", "maquina de lavar", "-1", "10");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("O custo deve ser maior que zero", e.getMessage());
            assertNull(produto1);
        }
    }

}
