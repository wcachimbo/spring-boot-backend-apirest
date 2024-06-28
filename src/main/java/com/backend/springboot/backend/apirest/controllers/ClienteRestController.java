package com.backend.springboot.backend.apirest.controllers;

import com.backend.springboot.backend.apirest.models.entity.Cliente;
import com.backend.springboot.backend.apirest.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = {"http://localhost:4200"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT } ,
        maxAge = 1800)
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")

    public ResponseEntity<?> show(@PathVariable Long id){
        Cliente cliente = null;
        Map<String, Object > response= new HashMap<>();
        try{
            cliente = clienteService.findById(id);
            if(cliente==null){
                response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" No existe en la base de datos!")));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
            }
        }catch (DataAccessException e){
            response.put("mensaje","Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(" :").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@RequestBody Cliente cliente){
        Cliente clienteNew = null;
        Map<String,Object> response = new HashMap<>();
        try{
            cliente.setCreateAt(new Date());
            clienteNew= clienteService.save(cliente);
            response.put("mensaje","El cliente ha sido creado con éxito!");
            response.put("cliente",clienteNew);
            return  new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);

        }catch (DataAccessException e){
            response.put("mensaje","Error al realizar el insert del usuario ".concat(cliente.toString()).concat(" en la base de datos"));
            response.put("error", e.getMessage().concat(" :").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        try{
            Cliente clienteActual= clienteService.findById(id);
            Cliente clienteUpdate= null;

            if(clienteActual==null){
                response.put("mensaje", "El No Se Pudo Editar, El Cliente ID : ".concat(id.toString().concat(" No Existe en la base de datos!")));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }else {
                clienteActual.setApellido(cliente.getApellido());
                clienteActual.setNombre(cliente.getNombre());
                clienteActual.setEmail(cliente.getEmail());
                clienteActual.setCreateAt(cliente.getCreateAt());
                clienteUpdate= clienteService.save(clienteActual);
                response.put("mensaje","El cliente ha sido Actualizado con éxito!");
                response.put("cliente",clienteUpdate);
                return  new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
            }
        }catch (DataAccessException e){
            response.put("mensaje","Error al actualizar el cliente  en la base de datos");
            response.put("error", e.getMessage().concat(" :").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();

        try{
        clienteService.delete(id);

    }catch (DataAccessException e){
        response.put("mensaje","Error al eliminar el cliente de la base de datos");
        response.put("error", e.getMessage().concat(" :").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
        response.put("mensaje","Cliente eliminado con éxito!");
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }
}
