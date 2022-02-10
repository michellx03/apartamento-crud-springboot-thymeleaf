package br.com.excus.apartamento.controller;

import br.com.excus.apartamento.domain.Apartamento;
import br.com.excus.apartamento.exception.ResourceNotFoundException;
import br.com.excus.apartamento.service.ApartamentoService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class ApartamentoController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
    private final int ROW_PER_PAGE = 5;
 
    @Autowired
    private ApartamentoService apartamentoService;
 
    @Value("${msg.title}")
    private String title;
 
    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
    	 model.addAttribute("title", title);
    	 return "index";
    }
 
    @GetMapping(value = "/api/apartamento")
    public String getApartamentos(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
    	
    	List<Apartamento> apartamento = apartamentoService.findAll(pageNumber, ROW_PER_PAGE);
     
        long count = apartamentoService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("apartamento", apartamento);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "apartamento-list";
    }
 
    @GetMapping(value = {"/api/apartamento/add"})
    public String showAddApartamento(Model model) {
        Apartamento apartamento = new Apartamento();
        model.addAttribute("add", true);
        model.addAttribute("apartamento", apartamento);
     
        return "apartamento-edit";
    }
     
    @PostMapping(value = "/api/apartamento/add")
    public String addApartamento(Model model, @ModelAttribute("apartamento") Apartamento apartamento) {        
        try {
            Apartamento newApartamento = apartamentoService.save(apartamento);
            return "redirect:/api/apartamento/" + String.valueOf(newApartamento.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "apartamento-edit";
        }        
    }
     
    @GetMapping(value = {"/api/apartamento/{apartamentoId}/edit"})
    public String showEditApartamento(Model model, @PathVariable long apartamentoId) {
        Apartamento apartamento = null;
        try {
            apartamento = apartamentoService.findById(apartamentoId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Apartamento não encontrado!");
        }
        model.addAttribute("add", false);
        model.addAttribute("apartamento", apartamento);
        return "apartamento-edit";
    }
     
    @PostMapping(value = {"/api/apartamento/{apartamentoId}/edit"})
    public String updateApartamento(Model model, @PathVariable long apartamentoId, @ModelAttribute("apartamento") Apartamento apartamento) {        
        try {
        	apartamento.setId(apartamentoId);
            apartamentoService.save(apartamento);//aqui
            return "redirect:/api/apartamento/" + String.valueOf(apartamento.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
     
             model.addAttribute("add", false);
            return "apartamento-edit";
        }
    }
    
    @GetMapping(value = "/api/apartamento/{apartamentoId}")
    public String getApartamentoById(Model model, @PathVariable long apartamentoId) {
        Apartamento apartamento = null;
        try {
            apartamento = apartamentoService.findById(apartamentoId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Apartamento não encontrado!");
        }
        model.addAttribute("apartamento", apartamento);
        return "apartamento";
    }
    
    @GetMapping(value = {"/api/apartamento/{apartamentoId}/delete"})
    public String showDeleteApartamentoById(Model model, @PathVariable long apartamentoId) {
        Apartamento apartamento = null;
        try {
        	apartamento = apartamentoService.findById(apartamentoId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Apartamento não encontrado!");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("apartamento", apartamento);
        return "apartamento";
    }
     
    @PostMapping(value = {"/api/apartamento/{apartamentoId}/delete"})
    public String deleteApartamentoById(Model model, @PathVariable long apartamentoId) {
        try {
        	apartamentoService.deleteById(apartamentoId);
            return "redirect:/api/apartamento";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "apartamento";
        }
    }
    
}
