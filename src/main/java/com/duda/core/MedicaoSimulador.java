package com.duda.core;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MedicaoSimulador {

    private final RestTemplate rest = new RestTemplate();

    // ESTADO DO SIMULADOR
    private double pesoAtual = 0;
    private double pesoDesejado = 0;
    private long idBalanca = 1L;
    private String placaAtual = "ABC-1234";
    private int fase = 0; // 0 = vazio, 1 = subindo, 2 = estabilizando
    private int ciclosEstaveis = 0;

//    @Scheduled(fixedRate = 100) // 100ms por medição
    public void enviarMedicaoFake() {

        switch (fase) {
            case 0 -> simularBalancaVazia();
            case 1 -> simularSubida();
            case 2 -> simularEstabilizacao();
        }

        Map<String, Object> body = Map.of(
                "id",idBalanca ,
                "weight", pesoAtual,
                "plate", placaAtual
        );

        rest.postForEntity("http://localhost:8080/api/leituras", body, Void.class);
    }

    private void simularBalancaVazia() {
        // Ruído pequeno
        pesoAtual = 0 + (Math.random() * 0.8);

        // De tempos em tempos inicia um ciclo
        if (Math.random() < 0.005) { // 0.5% de chance a cada 100ms (~20s)
            fase = 1;
            pesoDesejado = 28000 + Math.random() * 5000; // 28-33 toneladas
        }
    }

    private void simularSubida() {
        // Aumenta gradualmente até o peso final
        pesoAtual += (pesoDesejado - pesoAtual) * 0.2;

        // Ruído grande durante movimento
        pesoAtual += (Math.random() - 0.5) * 150;

        // Quando perto do alvo, vai para estabilização
        if (Math.abs(pesoDesejado - pesoAtual) < 300) {
            fase = 2;
            ciclosEstaveis = 0;
        }
    }

    private void simularEstabilizacao() {
        // Ruído pequeno (situação real de peso estabilizado)
        pesoAtual = pesoDesejado + (Math.random() - 0.5) * 10;

        if (++ciclosEstaveis > 150) {
            // ciclo completo, volta a ficar vazio
            fase = 0;
            pesoDesejado = 0;
        }
    }
}
