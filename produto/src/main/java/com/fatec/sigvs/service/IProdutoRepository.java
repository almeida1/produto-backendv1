package com.fatec.sigvs.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fatec.sigvs.model.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
	// Método personalizado para calcular o total da multiplicação do custo pela quantidade
    @Query("SELECT SUM(p.custo * p.quantidadeNoEstoque) FROM Produto p")
    Double calcularTotalCustoQuantidade();
	public List <Produto> findByDescricaoContaining(String descricao);
}
