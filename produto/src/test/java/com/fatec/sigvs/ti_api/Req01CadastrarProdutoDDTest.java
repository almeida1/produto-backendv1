package com.fatec.sigvs.ti_api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Req01CadastrarProdutoDDTest {
	@Autowired
	private TestRestTemplate testRestTemplate;
	ResponseEntity<String> result = null;
	@ParameterizedTest
	@CsvSource({
			"Eletrobomba 110V para Maquina de Lavar e Lava Louças, maquina de lavar, 51.66, 12, 201 CREATED, Sucesso",
			"' ', maquina de lavar, 51.66, 12, 400 BAD_REQUEST, A descrição não deve estar em branco", 
			", maquina de lavar, 51.66, 12,400 BAD_REQUEST, A descrição não deve estar em branco",
			"Eletrobomba 110V para Maquina de Lavar e Lava Louças, , 51.66, 12, 400 BAD_REQUEST, A categoria não deve estar em branco"
	})

	void leDados(String descricao, String categoria, String custo, String quant, String re1, String re2) {

		final String baseUrl = "/api/v1/produtos";
		HttpHeaders headers = new HttpHeaders();
		record ProdutoDTO(String descricao, String categoria, String custo, String quantidadeNoEstoque) {};
		ProdutoDTO produto = new ProdutoDTO(descricao, categoria, custo, quant);
		
		HttpEntity<ProdutoDTO> request = new HttpEntity<>(produto, headers);
		result = testRestTemplate.postForEntity(baseUrl, request, String.class);
		
		assertEquals(re1, result.getStatusCode().toString());
		
		if ((result.getStatusCode().toString().equals("400 BAD_REQUEST"))) {
			assertEquals(re2, result.getBody().toString());
		}
	}

}
