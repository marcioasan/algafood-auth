package com.algaworks.algafood.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

//19.17. Corrigindo links de paginação com ordenação - 6'30"

public class PageWrapper<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;

	private Pageable pageable;
	
	public PageWrapper(Page<T> page, Pageable pageable) {
		super(page.getContent(), pageable, page.getTotalElements());
		
		this.pageable = pageable;
	}
	
	@Override
	public Pageable getPageable() {
		return this.pageable;
	}
}
