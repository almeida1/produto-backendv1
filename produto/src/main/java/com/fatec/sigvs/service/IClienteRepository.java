package com.fatec.sigvs.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.sigvs.model.Cliente;
@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

}
