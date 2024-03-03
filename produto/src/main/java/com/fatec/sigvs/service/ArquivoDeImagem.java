package com.fatec.sigvs.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class ArquivoDeImagem implements MultipartFile {
	private byte[] input;

	public ArquivoDeImagem(byte[] input) {
		this.input = input;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
	
		return input.length == 0;
	}

	@Override
	public long getSize() {
		return input.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return input;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub

	}

}
