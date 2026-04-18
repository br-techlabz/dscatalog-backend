package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
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
}
