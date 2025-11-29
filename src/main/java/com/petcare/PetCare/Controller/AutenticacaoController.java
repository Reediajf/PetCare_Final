package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.UsuarioDTO;
import com.petcare.PetCare.Model.Usuario;
import com.petcare.PetCare.Security.JwtUtil;
import com.petcare.PetCare.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private  UsuarioService usuarioService;

    @Autowired
    private  JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO loginDTO) {
        Usuario usuario = usuarioService.buscarPorEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        return ResponseEntity.ok("Bearer " + token);
    }
}


