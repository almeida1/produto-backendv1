package com.fatec.sigvs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.sigvs.model.Catalogo;

import com.fatec.sigvs.model.IProdutoRepository;
import com.fatec.sigvs.model.Imagem;

import com.fatec.sigvs.model.Produto;

@Service
public class ProdutoServico implements IProdutoServico {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	IProdutoRepository produtoRepository;
	@Autowired
	IImagemServico imagemServico;
	

	@Override
	public List<Catalogo> consultaPorDescricao(String descricao) {
		if (descricao.isBlank() || descricao.isEmpty() ) {
			return new ArrayList<>();
		} else {
			logger.info(">>>>>> servico consultar por descricao " + descricao);
			List<Produto> produto = produtoRepository.findByDescricaoContaining(descricao);
			logger.info(">>>>>> servico consultar por descricao achou produto=> " + produto.size());
			List<Catalogo> catalogo = consultaCatalogo();
			logger.info(">>>>>> servico consultar por descricao achou catalogo=> " + catalogo.size());
			List<Catalogo> lista = new ArrayList<>();
			for (Catalogo c : catalogo) {
				for (Produto p : produto) {
					if (c.getId().equals(p.getId())) {
						lista.add(c);
					}
				}

			}
			return lista;
		}
	}

	@Override
	public Optional<Produto> cadastrar(Produto produto) {
		logger.info(">>>>>> servico cadastrar produto iniciado ");
		return Optional.ofNullable(produtoRepository.save(produto));
	}

	@Override
	public Optional<Produto> consultarPorId(String id) {
		logger.info(">>>>>> servico consulta por id chamado");
		long codProduto = Long.parseLong(id);
		return produtoRepository.findById(codProduto);
	}

	@Override
	public Optional<Produto> atualizar(Long produtoId, Produto produtoAtualizado) {
		logger.info(">>>>>> servico atualizar informacoes de produto chamado");
		Produto produto = produtoRepository.findById(produtoId)
				.orElseThrow(() -> new IllegalArgumentException("Produto nao cadastrado"));
		produto.setDescricao(produtoAtualizado.getDescricao());
		produto.setCategoria(produtoAtualizado.getCategoria());
		produto.setQuantidadeNoEstoque(produtoAtualizado.getQuantidadeNoEstoque());
		produto.setCusto(produtoAtualizado.getCusto());
		
		return Optional.ofNullable(produtoRepository.save(produto));
	}

	@Override
	public void excluir(String id) {
		long codProduto = Long.parseLong(id);
		produtoRepository.deleteById(codProduto);
	}

	/**
	 * associa o id do produto ao id da imagem e adiciona no catalogo de produtos
	 * retorna - lista de produtos com a imagem
	 */
	public List<Catalogo> consultaCatalogo() {
		logger.info(">>>>>> servico consulta catalogo iniciado");
		Catalogo c = null;
		List<Catalogo> lista = new ArrayList<>();
		List<Produto> listaP = produtoRepository.findAll();
		logger.info(">>>>>> servico consulta catalogo qtde de produto =>" + produtoRepository.count());
		List<Imagem> listaI = imagemServico.getAll();
		logger.info(">>>>>> servico consulta catalogo qtde de imagens =>" + imagemServico.getAll().size());
		
		for (Produto p : listaP) {
            boolean achou = false;
            for (Imagem i : listaI) {
                logger.info(">>>>>> servico consulta catalogo id =>" + p.getId() + "-" + i.getId());
                if (p.getId().equals(i.getId())) {
                    lista.add(new Catalogo(p.getId(), p.getDescricao(), p.getCategoria(), p.getCusto(),
                            p.getQuantidadeNoEstoque(), i.getArquivo()));
                    achou = true;
                    break; // Parar de procurar imagens para este produto após encontrar uma correspondência
                }
            }
            if (!achou) { // Se não houver correspondência de imagem para o produto
                lista.add(new Catalogo(p.getId(), p.getDescricao(), p.getCategoria(), p.getCusto(),
                        p.getQuantidadeNoEstoque(), null));
            }
        }
		logger.info(">>>>>> servico consulta catalogo catalogo =>" + lista.size());
		return lista;
	}
	public List<Produto> consultaProduto() {
		List<Produto> listaDeProdutos = produtoRepository.findAll();
		return listaDeProdutos;
	}

	
	
}
