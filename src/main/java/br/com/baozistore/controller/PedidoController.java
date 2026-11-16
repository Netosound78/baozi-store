package br.com.baozistore.controller;

import br.com.baozistore.model.Pedido;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository repository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    /** POST /pedidos - registra um pedido (um cliente compra um produto em certa quantidade) */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        String erro = validar(pedido);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        pedido.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(pedido));
    }

    /** GET /pedidos - lista todos os pedidos */
    @GetMapping
    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    /** GET /pedidos/{id} - consulta pedido por ID */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /pedidos/{id} - atualiza um pedido (opcional) */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Pedido dados) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        String erro = validar(dados);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        dados.setId(id);
        return ResponseEntity.ok(repository.save(dados));
    }

    /** DELETE /pedidos/{id} - apaga um pedido */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Garante que o cliente e o produto informados existem e que a quantidade é válida. */
    private String validar(Pedido pedido) {
        if (pedido.getClienteId() == null || !clienteRepository.existsById(pedido.getClienteId())) {
            return "Cliente não encontrado (clienteId inválido)";
        }
        if (pedido.getProdutoId() == null || !produtoRepository.existsById(pedido.getProdutoId())) {
            return "Produto não encontrado (produtoId inválido)";
        }
        if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0) {
            return "Quantidade deve ser maior que zero";
        }
        return null;
    }
}
