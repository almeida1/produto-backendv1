package com.fatec.sigvs.ti_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IProdutoRepository;
@SpringBootTest
class Req03ConsultarProdutoPorIdTest {
	@Autowired
	IProdutoRepository repository;
	@Test
	public void setup() {
		repository.deleteAll();
        Produto produto1 = new Produto("eletrobomba 110v", "maquina de lavar", "22.30", "10");
        Produto produto2 = new Produto("Tirante Original Brastemp E Consul De 7 A 12 Kg 11cm", "lavar louça", "3.90", "20");
        Produto produto3 = new Produto("Termoatuador Lavadora Colormaq Electrolux GE", "maquina de lavar", "29.70", "40");
        repository.saveAll(Arrays.asList(produto1, produto2, produto3));
       
		
	}
    public void ct01_quando_exitem_produtos_cadastrados_consulta_por_id_retorna_true() {
    	setup();
    	assertTrue(repository.findById(1L).isPresent());
    }
    
}
