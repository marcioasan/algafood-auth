package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.core.validation.FileContentType;
import com.algaworks.algafood.core.validation.FileSize;

import lombok.Getter;
import lombok.Setter;

//14.2. Implementando upload de arquivo com multipart/form-data - 1'30", 7'20", 8'40"

@Getter
@Setter
public class FotoProdutoInput {
	
	@NotNull
	@FileSize(max = "500KB")
	@FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_PDF_VALUE})
	private MultipartFile arquivo;
	
	@NotBlank
	private String descricao;
}
