package com.fatec.sigvs.service;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.sigvs.model.Imagem;

@Repository
public interface IImagemRepository extends JpaRepository<Imagem, Long> {
	Optional<Imagem> findByNome(String nome);
}