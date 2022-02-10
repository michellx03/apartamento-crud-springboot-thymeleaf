package br.com.excus.apartamento.service;

import java.util.ArrayList;

import br.com.excus.apartamento.domain.Apartamento;
import br.com.excus.apartamento.exception.BadResourceException;
import br.com.excus.apartamento.exception.ResourceAlreadyExistsException;
import br.com.excus.apartamento.exception.ResourceNotFoundException;
import br.com.excus.apartamento.repository.ApartamentoRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
 
@Service
public class ApartamentoService {
    
    @Autowired
    private ApartamentoRepository apartamentoRepository;
    
    private boolean existsById(Long id) {
        return apartamentoRepository.existsById(id);
    }
    
    public Apartamento findById(Long id) throws ResourceNotFoundException {
        Apartamento apartamento = apartamentoRepository.findById(id).orElse(null);
        if (apartamento==null) {
            throw new ResourceNotFoundException("Não foi encontrado um Apartamento com o id: " + id);
        }
        else return apartamento;
    }
    
    public List<Apartamento> findAll(int pageNumber, int rowPerPage) {
        List<Apartamento> apartamentos = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, 
                Sort.by("id").ascending());
        apartamentoRepository.findAll(sortedByIdAsc).forEach(apartamentos::add);
        return apartamentos;
    }
    
    public Apartamento save(Apartamento apartamento) throws BadResourceException, ResourceAlreadyExistsException, ResourceNotFoundException {  	
    	
    	Apartamento apartamentoByNumero = apartamentoRepository.findByNumero(apartamento.getNumero());
    		
    	if(StringUtils.isEmpty(apartamento.getNumero())) {
    		BadResourceException exc = new BadResourceException("Apartamento não pode ser vazio");
            exc.addErrorMessage("Apartamento não pode ser vazio");
            throw exc;
    	} else if (StringUtils.isEmpty(apartamento.getEstado())) {
    		BadResourceException exc = new BadResourceException("Estado não pode ser vazio");
            exc.addErrorMessage("Estado não pode ser nulo nem vazio");
            throw exc;
    	} else if (apartamentoByNumero != null && apartamento.getId() != apartamentoByNumero.getId()) {
    		BadResourceException exc = new BadResourceException("Apartamento não pode ser repetido");
            exc.addErrorMessage("Numero do apartamento não pode ser vazio");
            throw exc;
    	}else {
             return apartamentoRepository.save(apartamento);
    	}
    }

    
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Não foi encontrado um Apartamento com o id: " + id);
        }
        else {
            apartamentoRepository.deleteById(id);
        }
    }
    
    public Long count() {
        return apartamentoRepository.count();
    }
}