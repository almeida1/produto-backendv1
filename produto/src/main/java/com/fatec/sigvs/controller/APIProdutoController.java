package com.fatec.sigvs.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fatec.sigvs.model.Imagem;
import com.fatec.sigvs.model.Produto;
import com.fatec.sigvs.service.IProdutoServico;
import com.fatec.sigvs.service.ImagemServico;

@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/produtos")
public class APIProdutoController {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private IProdutoServico produtoServico;
	@Autowired
	ImagemServico imagemServico;

	// A anotação @RequestBody indica que o Spring deve desserializar o body da
	// solicitação em um objeto. Este objeto é passado como um parâmetro do método

	@PostMapping
	public ResponseEntity<Object> cadastraProduto(@RequestBody Produto p) {
		logger.info(">>>>>> apicontroller cadastrar produto iniciado...");
		Optional<Produto> produto = produtoServico.cadastrar(p);
//		if (produto.isPresent()) {
//			return ResponseEntity.status(HttpStatus.CREATED).body(produto.get());
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro não esperado ");
//		}
		return ResponseEntity.status(HttpStatus.CREATED).body(produto.get());
	}

	// atualiza informacoes de produto
	@PutMapping("{id}")
	public ResponseEntity<Object> atualizaProduto(@PathVariable("id") Long produtoId,
			@RequestBody Produto produtoAtualizado) {
		logger.info(">>>>>> apicontroller atualizar informacoes de produto iniciado " + produtoId);
		try {
			Produto produto = produtoServico.atualizar(produtoId, produtoAtualizado).get();
			return ResponseEntity.ok(produto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Retorna (i) detalhes do produto ou (ii) nao encontrado se o codigo do produto
	 * nao existir
	 * 
	 * @param id - codigo do produto enviado pela aplicacao cliente
	 * @return - 200 OK ou 404 NOT_FOUND
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Object> consultaPorId(@PathVariable String id) {
		logger.info(">>>>>> apicontroller consulta por id chamado");
		Optional<Produto> produto = produtoServico.consultarPorId(id);
		if (produto.isEmpty()) {
			logger.info(">>>>>> apicontroller id not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id não encontrado.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(produto.get());
	}

	@PostMapping("/imadb")
	public ResponseEntity<String> upload(@RequestParam(value = "file") MultipartFile file, @RequestParam String id) {
		logger.info(">>>>>> apicontroller upload iniciada...");
		try {
			logger.info(">>>>>> api manipula file upload chamou servico salvar");
			long codProduto = Long.parseLong(id);
			Optional<Imagem> i = imagemServico.salvar(file, codProduto);
			if (i.isPresent()) {
				return ResponseEntity.ok().body("Imagem enviada com sucesso");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalido não localizado");
			}
		} catch (FileNotFoundException e) {
			logger.info(">>>>>> api manipula file upload arquivo não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo nao encontrado");
		} catch (FileUploadException e) {
			logger.info(">>>>>> api manipula file upload erro no upload");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao enviar o arquivo");
		} catch (IOException e) {
			logger.info(">>>>>> api manipula file upload erro de i/o => " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha erro de I/O");
		} catch (NumberFormatException e) {
			logger.info(">>>>>> api manipula file upload erro de i/o => " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalido");
		}
	}

	@GetMapping("/imadb/{nomeArquivo}")
	public ResponseEntity<Object> download(@PathVariable String nomeArquivo) {
		logger.info(">>>>>> api download iniciado..." + nomeArquivo);
		try {
			logger.info(">>>>>> api download nome do arquivo=>" + nomeArquivo);
			byte[] arquivo = imagemServico.getImagem(nomeArquivo);
			logger.info(">>>>>> api download =>" + arquivo.length);
			return ResponseEntity.status(HttpStatus.OK).body(arquivo);
		} catch (Exception e) {
			logger.info(">>>>>> api download dados invalidos - " + nomeArquivo + "-" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados invalidos");
		}
	}

	@GetMapping("/imadb/{id}/id")
	public ResponseEntity<Object> downloadById(@PathVariable Long id) {
		logger.info(">>>>>> api download iniciado..." + id);
		try {
			logger.info(">>>>>> api download nome do arquivo=>" + id);
			byte[] arquivo = imagemServico.getImagemById(id);
			logger.info(">>>>>> api download =>" + arquivo.length);
			return ResponseEntity.status(HttpStatus.OK).body(arquivo);
		} catch (Exception e) {
			logger.info(">>>>>> api download dados invalidos - " + id + "-" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados invalidos");
		}
	}

	/**
	 * consulta todos considerando id.produto = id.imagem
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Object> consultaProduto() {
		return ResponseEntity.status(HttpStatus.OK).body(produtoServico.consultaProduto());
	}

	/**
	 * consulta todos considerando id.produto = id.imagem
	 * 
	 * @return
	 */
	@GetMapping("/catalogo")
	public ResponseEntity<Object> consultaCatalogo() {
		return ResponseEntity.status(HttpStatus.OK).body(produtoServico.consultaCatalogo());
	}

	@GetMapping("/imadb/")
	public ResponseEntity<Object> obtemImagens() {
		return ResponseEntity.status(HttpStatus.OK).body(imagemServico.getAll());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> excluiProduto(@PathVariable String id) {
		logger.info(">>>>>> apicontroller exluir por id chamado");
		Optional<Produto> produto = produtoServico.consultarPorId(id);
		if (produto.isEmpty()) {
			logger.info(">>>>>> apicontroller id not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id não encontrado.");
		} else {
			produtoServico.excluir(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		}

	}

	@GetMapping("/imobilizado")
	public ResponseEntity<Object> calculaImobilizado() {
		logger.info(">>>>>> apicontroller consulta imobilizado");
		return ResponseEntity.status(HttpStatus.OK).body(produtoServico.estoqueImobilizado());
		
	}
}
