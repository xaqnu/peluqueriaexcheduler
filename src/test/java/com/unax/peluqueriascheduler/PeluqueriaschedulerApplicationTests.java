package com.unax.peluqueriascheduler;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class PeluqueriaschedulerApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void citas_can_be_created() {
		Cita cita = new Cita(1, 1, 2, 3,  LocalDateTime.now(), LocalDateTime.now().plusHours(1), EstadoCita.PENDIENTE);
		assertThat(cita.clientId()).isEqualTo(1);
		assertThat(cita.peluqueroId()).isEqualTo(2);
		assertThat(cita.tipoServicioId()).isEqualTo(3);
		// Aquí puedes agregar pruebas para verificar que las citas se crean correctamente
	}

}
