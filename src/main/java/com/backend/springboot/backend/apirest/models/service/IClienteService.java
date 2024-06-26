package com.backend.springboot.backend.apirest.models.service;

import com.backend.springboot.backend.apirest.models.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IClienteService {
    public List<Cliente> findAll();
    public Cliente findById(Long id);
    public Cliente save(Cliente cliente);
    public void delete(Long id);

}
