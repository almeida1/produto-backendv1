package com.fatec.sigvs.service;
import java.util.List;
import java.util.Optional;

import com.fatec.sigvs.model.Catalogo;

import com.fatec.sigvs.model.Produto;

public interface IProdutoServico {
	public List<Catalogo> consultaCatalogo();
	public List<Produto> consultaProduto();
	public List<Catalogo> consultaPorDescricao(String descricao);
	public Optional <Produto> cadastrar(Produto produto);
	public Optional <Produto> consultarPorId(String id);
	public Optional <Produto> atualizar(Long id, Produto produto);
	public void excluir(String id);
	
}
