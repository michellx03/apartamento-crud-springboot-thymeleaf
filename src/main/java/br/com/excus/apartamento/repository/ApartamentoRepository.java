package br.com.excus.apartamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.excus.apartamento.domain.Apartamento;

public interface ApartamentoRepository extends PagingAndSortingRepository<Apartamento, Long>, JpaSpecificationExecutor<Apartamento> {
	
	Apartamento findByNumero(Integer numero);

}
