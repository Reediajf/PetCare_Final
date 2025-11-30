package com.petcare.PetCare.Service;

import com.petcare.PetCare.DTO.TutorDTO;
import com.petcare.PetCare.Model.Tutor;
import com.petcare.PetCare.Repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TutorService {
    @Autowired
    private  TutorRepository tutorRepository;



    @Transactional
    public Tutor cadastrar(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    @Transactional
    public Tutor Atualizar(Long id, Tutor tutorAtualizado) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));

        tutor.setNome(tutorAtualizado.getNome());
        tutor.setEmail(tutorAtualizado.getEmail());

        return tutorRepository.save(tutor);
    }

    @Transactional
    public void excluir(Long id) {
        tutorRepository.deleteById(id);
    }

    public List<Tutor> listarTutores() {
        return (List<Tutor>) tutorRepository.findAll();
    }

    public Tutor buscarPorEmail(String email) {
        return tutorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));
    }

    public Tutor buscarPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));
    }

    public String buscarEmail(Long id) {
        return tutorRepository.findEmailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor não encontrado"));
    }

    public Tutor converterDtoParaEntidade(TutorDTO dto) {
        Tutor tutor = new Tutor();
        tutor.setNome(dto.getNome());
        tutor.setEmail(dto.getEmail());

        return tutor;
    }

}
