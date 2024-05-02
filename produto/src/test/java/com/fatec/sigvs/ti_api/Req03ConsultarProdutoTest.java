package com.fatec.sigvs.ti_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IImagemRepository;
import com.fatec.sigvs.service.IProdutoRepository;
import com.google.gson.Gson;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Req03ConsultarProdutoTest {
	String urlBase = "/api/v1/produtos/";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	IImagemRepository imagemRepository;
	@Autowired
	IProdutoRepository repository;
	/**
	 * cadastrar um produto e uma imagem associa o id do produto ao id da imagem
	 */
	public void setup() {
		byte[] arquivo1 = null;
		Produto produto1 = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
				"51.66", "12");
		repository.save(produto1);
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
	void ct01_quando_consulta_por_id_retorna_detalhaes_do_produto() throws Exception {
		// Dado - que id esta cadastrado
		setup();
		// Quando - o usuario consulta o id
		String id = "1";
		ResponseEntity<String> resposta = testRestTemplate.getForEntity(urlBase + id, String.class);
		// Entao - retorna 200 ok e as informacoes detalhadas do produto);
		Gson gson = new Gson();
		Produto re = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar", "51.66", "12");
		Produto ro = gson.fromJson(resposta.getBody(), Produto.class);
		assertEquals("200 OK", resposta.getStatusCode().toString());
		System.out.println(ro.toString());
		assertTrue(re.equals(ro));
	}

	@Test
	void ct02_quando_consulta_por_id_nao_cadastrado_retorna_erro() throws Exception {
		// Dado - que id nao esta cadastrado
		String id = "99";
		// Quando - o usuario consulta o id
		ResponseEntity<String> resposta = testRestTemplate.getForEntity(urlBase + id, String.class);
		// Entao - retorna not found
		assertEquals("404 NOT_FOUND", resposta.getStatusCode().toString());
		assertEquals("Id não encontrado.", resposta.getBody());
	}
//	 @Test
//    public void findAllCustomers() throws Exception {
//        ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
//               "/Customer", HttpMethod.GET, null,
//               new ParameterizedTypeReference<List<Customer>>(){});
//        List<Customer> list = responseEntity.getBody();
//        Assert.assertEquals(list.size(), 0);
//    }


}
