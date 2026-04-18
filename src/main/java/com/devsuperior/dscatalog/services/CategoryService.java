package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){		
		
		List<Category> lista = repository.findAll(); 
		List<CategoryDTO> listaDTO = new ArrayList<>();
		for(Category cat : lista) {
			listaDTO.add(new CategoryDTO(cat));
		}
		return listaDTO;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category cat = new Category();
		cat.setName(dto.getName());		
		cat = repository.save(cat);
		
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
			
		} catch (javax.persistence.EntityNotFoundException ex) {
			throw new EntityNotFoundException("Categoria não encontrada");
		}
		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Categoria não encontrada");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Existem produtos associados à categoria");
		}
		
		
	}

	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<Category> lista = repository.findAll(pageRequest);
		return lista.map(x -> new CategoryDTO(x));
	}
}
