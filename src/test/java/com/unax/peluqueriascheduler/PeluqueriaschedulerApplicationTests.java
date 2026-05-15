package com.unax.peluqueriascheduler;

import java.time.LocalDateTime;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;
import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PeluqueriaschedulerApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void citas_can_be_created() {
		Cita cita = new Cita(
			1,
			1,
			2,
			3,
		    LocalDateTime.now(), 
			LocalDateTime.now().plusHours(1), 
			EstadoCita.PENDIENTE
		                  );
		assertThat(cita.clientId()).isEqualTo(1);
		assertThat(cita.peluqueroId()).isEqualTo(2);
		assertThat(cita.tipoServicioId()).isEqualTo(3);
		// Aquí puedes agregar pruebas para verificar que las citas se crean correctamente
	}
	@Test
	void cita_cannot_have_nonpositive_id() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				-1,
				1,
				2,
				3,
			    LocalDateTime.now(), 
				LocalDateTime.now().plusHours(1), 
				EstadoCita.PENDIENTE
			);
		});
	}

	@Test
	void cita_cannot_have_null_timestamp_Inicio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				1,
				2,
				3,
			    null, 
				LocalDateTime.now().plusHours(1), 
				EstadoCita.PENDIENTE
			);
		});	
	} 
	@Test
	void cita_cannot_have_null_timestamp_Fin() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				1,
				2,
				3,
			    LocalDateTime.now(), 
				null, 
				EstadoCita.PENDIENTE
			);
		});	
	}
	@Test
	void cita_cannot_have_timestampFin_before_timestampInicio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				1,
				2,
				3,
			    LocalDateTime.now(), 
				LocalDateTime.now().minusHours(1), 
				EstadoCita.PENDIENTE
			);
		});	
	}

	@Test
	void cita_cannot_have_null_estado() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				1,
				2,
				3,
			    LocalDateTime.now(), 
				LocalDateTime.now().plusHours(1), 
				null
			);
		});	
	}

	@Test
	void cita_cannot_have_non_positive_clientId() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				-1,
				2,
				3,
			    LocalDateTime.now(), 
				LocalDateTime.now().plusHours(1), 
				EstadoCita.PENDIENTE
			);
		});	
	}
	@Test
	void cita_cannot_have_non_positive_peluqueroId() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(
				1,
				1,
				-2,
				3,
			    LocalDateTime.now(), 
				LocalDateTime.now().plusHours(1), 
				EstadoCita.PENDIENTE
			);
		});	
	}
	@Test
	void cita_cannot_have_non_positive_tipoServicioId() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cita(	
				1,
				1,
				2,
				-3,
			    LocalDateTime.now(), 
				LocalDateTime.now().plusHours(1), 
				EstadoCita.PENDIENTE
			);
		});
	}

	@Test
	void citaRequest_can_be_created() {
		CitaRequest citaRequest = new CitaRequest(
			"Corte de pelo",
		    LocalDateTime.of(2026,2, 3, 0, 0, 0, 0)
		);
		assertThat(citaRequest.tipoServicio()).isEqualTo("Corte de pelo");
		assertThat(citaRequest.timestampInicio()).isEqualTo(LocalDateTime.of(2026, 2, 3, 0, 0, 0, 0));
	}

	@Test
	void cliente_can_be_created() {
		Cliente cliente1 = new Cliente(
			1,
			"Unax",
			"unax@example.com"
		);
		Cliente cliente2 = new Cliente(
			2,
			"Unax",
			"unax2@example.com",
			"123456789"
		);
		
		assertThat(cliente1.id()).isEqualTo(1);
		assertThat(cliente1.nombre()).isEqualTo("Unax");
		assertThat(cliente1.email()).isEqualTo("unax@example.com");
		assertThat(cliente1.telefono()).isNull();
		assertThat(cliente2.id()).isEqualTo(2);
		assertThat(cliente2.telefono()).isEqualTo("123456789");
	}
	
}

