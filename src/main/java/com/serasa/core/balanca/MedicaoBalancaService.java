package com.serasa.core.balanca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* Seria bom fazer:
*       - controle de status : ESPERANDO_CALIBRAÇÃO, DESATIVADA, EM_USO
*/

@Service
public class MedicaoBalancaService {

    private final MedicaoBalancaRepository medicaoBalancaRepository;

    @Autowired
    public MedicaoBalancaService(MedicaoBalancaRepository medicaoBalancaRepository) {this.medicaoBalancaRepository = medicaoBalancaRepository;}

     public void createMedicaoBalanca(MedicaoBalancaCreateDto newMedicao) {

        MedicaoBalancaEntity medicao = new MedicaoBalancaEntity(
                newMedicao.getId(),
                newMedicao.getPlate(),
                newMedicao.getWeight()
        );

        medicaoBalancaRepository.save(medicao);
    }
}
