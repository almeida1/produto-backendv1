package com.fatec.sigvs.service;


import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.fatec.sigvs.model.Produto;

@Configuration
public class LoadDatabase {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	IImagemRepository imagemRepository;

	@Bean
	CommandLineRunner initDatabase(IProdutoRepository repository) {
		return args -> {
			Produto produto1 = new Produto("Eletrobomba 110V para Maquina de Lavar e Lava Louças", "maquina de lavar",
					"51.66", "12");
			Produto produto2 = new Produto("Tirante Original Brastemp E Consul De 7 A 12 Kg 11cm", "lavar louça", "3.90",
					"20");
			Produto produto3 = new Produto("Termoatuador Lavadora Colormaq Electrolux GE", "maquina de lavar", "29.70",
					"40");
			repository.saveAll(Arrays.asList(produto1, produto2, produto3));
			logger.info(">>>>> loaddatabase -> 3 produtos cadastrados no db.");
			
		
		};
	}

}