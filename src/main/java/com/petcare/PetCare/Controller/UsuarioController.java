package com.petcare.PetCare.Controller;


import com.petcare.PetCare.DTO.UsuarioDTO;
import com.petcare.PetCare.Model.Usuario;
import com.petcare.PetCare.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioService.converterDtoParaEntidade(usuarioDTO);
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario salvo = usuarioService.cadastrar(usuario);

        UsuarioDTO resposta = new UsuarioDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                null
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuarioAtualizado = usuarioService.converterDtoParaEntidade(usuarioDTO);

        // Criptografa a senha antes de atualizar
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioAtualizado.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        Usuario atualizado = usuarioService.Atualizar(id, usuarioAtualizado);

        UsuarioDTO resposta = new UsuarioDTO(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getEmail(),
                null
        );

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> lista = usuarioService.listarUsuario()
                .stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        null // Não retorna a senha na resposta
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        UsuarioDTO resposta = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                null
        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);

        UsuarioDTO resposta = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                null
        );

        return ResponseEntity.ok(resposta);
    }
}