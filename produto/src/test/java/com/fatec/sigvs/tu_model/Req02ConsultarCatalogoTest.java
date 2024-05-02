package com.fatec.sigvs.tu_model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.sigvs.model.Catalogo;
import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IImagemRepository;
import com.fatec.sigvs.service.IProdutoRepository;
import com.fatec.sigvs.service.IProdutoServico;
@SpringBootTest
class Req02ConsultarCatalogoTest {
	@Autowired
	private IProdutoServico produtoServico;
	@Autowired
	IImagemRepository imagemRepository;
	@Autowired
	IProdutoRepository produtoRepository;
	
	
	public void setup() {
		byte[] arquivo1 = null;
		Produto produto1 = new Produto("Eletrobomba para maquina de lavar", "maquina de lavar", "51.66", "12");
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
		System.out.println("quantidade de registros no repositorio imagem=>" + imagemRepository.count());
	}
	@Test
	public void ct01_quando_existem_itens_no_catalogo_consulta_retorna_vazio_false() {
		setup();
		List<Catalogo> catalogo = produtoServico.consultaCatalogo();
		assertFalse(catalogo.isEmpty());
	}
	@Test
	public void ct02_quando_descricao_valida_consulta_retorna_vazio_false() {
		setup();
		List<Catalogo> catalogo = produtoServico.consultaPorDescricao("para");
		assertFalse(catalogo.isEmpty());
	}
}
