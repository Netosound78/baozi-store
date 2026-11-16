package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    /** POST /clientes - cria um novo registro */
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        cliente.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(cliente));
    }

    /** GET /clientes - lista todos os registros */
    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    /** GET /clientes/{id} - consulta por ID */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /clientes/{id} - atualiza um registro (opcional) */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return repository.findById(id).map(existente -> {
            existente.setNome(dados.getNome());
            existente.setClienteDesde(dados.getClienteDesde());
            return ResponseEntity.ok(repository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /clientes/{id} - apaga um registro */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
