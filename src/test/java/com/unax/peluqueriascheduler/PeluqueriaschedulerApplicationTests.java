package com.unax.peluqueriascheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.unax.peluqueriascheduler.dbfilters.CitasFilter;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;
import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;
import com.unax.peluqueriascheduler.repositories.CitasRepository;
import com.unax.peluqueriascheduler.utils.TimeInterval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class PeluqueriaschedulerApplicationTests {

	@Autowired
    private CitasRepository repository;
	@Test
	void contextLoads() {
	}
	@Test
	void citasCanBeCreated() {
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
	void citaCannotHaveNonpositiveId() {
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
	void citaCannotHaveNullTimestampInicio() {
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
	void citaCannotHaveNullTimestampFin() {
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
	void citaCannotHaveTimestampFinBeforeTimestampInicio() {
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
	void citaCannotHaveNullEstado() {
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
	void citaCannotHaveNonPositiveClientId() {
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
	void citaCannotHaveNonPositivePeluqueroId() {
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
	void citaCannotHaveNonPositiveTipoServicioId() {
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
	void citaRequestCanBeCreated() {
		CitaRequest citaRequest = new CitaRequest(
			"Corte de pelo",
		    LocalDateTime.of(2026,7, 3, 0, 0, 0, 0)
		);
		assertThat(citaRequest.tipoServicio()).isEqualTo("Corte de pelo");
		assertThat(citaRequest.timestampInicio()).isEqualTo(LocalDateTime.of(2026, 7, 3, 0, 0, 0, 0));
	}

	@Test
	void clienteCanBeCreated() {
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
	@Test
	void peluqueroCanBeCreated() {
		Peluquero peluquero = new Peluquero(
			1,
			"Pepe",
			"pepe@example.com",
			"987654321"
		);
		Peluquero peluquero2 = new Peluquero(
			2,
			"Juan",
			"juan@example.com"
		);
		assertThat(peluquero.id()).isEqualTo(1);
		assertThat(peluquero.nombre()).isEqualTo("Pepe");
		assertThat(peluquero.email()).isEqualTo("pepe@example.com");
		assertThat(peluquero.telefono()).isEqualTo("987654321");

		assertThat(peluquero2.id()).isEqualTo(2);
		assertThat(peluquero2.nombre()).isEqualTo("Juan");
		assertThat(peluquero2.email()).isEqualTo("juan@example.com");
		assertThat(peluquero2.telefono()).isNull();
	}
	@Test
	void citasCanbeCreatedInDatabase() {
		Cliente cliente = new Cliente(1, "Unax", "unax@example.com");
		Peluquero peluquero = new Peluquero(1, "Pepe", "pepe@example.com", "987654321");
		CitaRequest citaRequest = new CitaRequest("Corte", LocalDateTime.now().plusDays(1));
		Cita cita = repository.createCita(citaRequest, cliente, peluquero);
		assertThat(cita).isNotNull();
		assertThat(cita.clientId()).isEqualTo(cliente.id());
		assertThat(cita.peluqueroId()).isEqualTo(peluquero.id());
		assertThat(cita.tipoServicioId()).isGreaterThan(0);
		assertThat(cita.timestampInicio()).isEqualTo(citaRequest.timestampInicio());
		assertThat(cita.estado()).isEqualTo(EstadoCita.PENDIENTE);	
	}

	@Test
	void citasCanbeRetrievedFromDatabase() {
		Cita cita = repository.getCitaById(1);
		assertThat(cita).isNotNull();
		assertThat(cita.id()).isEqualTo(1);
	}
    @Test
	void citasCanBeRetrievedByCliente() {
	    Cliente cliente = new Cliente(1, "Juan Pérez", "juan@test.com");
		List<Cita> citas = repository.getCitasByCliente(cliente);
		assertThat(citas).isNotNull();
		boolean clienteCorrecto = citas.stream().allMatch((x) -> x.clientId()==cliente.id());
		assertTrue(clienteCorrecto);

	}
	@Test
	void citasCanBeRetrievedByPeluquero(){
		Peluquero peluquero = new Peluquero(1, "María García", "maria@test.com");
		List<Cita> citas =repository.getCitasByPeluquero(peluquero);
		assertThat(citas).isNotNull();
		boolean peluqueroCorrecto = citas.stream().allMatch((x) -> x.peluqueroId()==peluquero.id());
		assertTrue(peluqueroCorrecto);
		
	}
	@Test
	void citasFilterCanBeCreated(){
		Map<Integer,List<TimeInterval>>rangosHorarios = new HashMap<>();
		rangosHorarios.put(1, List.of(new TimeInterval(LocalTime.of(8, 0),LocalTime.of(15, 0))));
		rangosHorarios.put(2, List.of(new TimeInterval(LocalTime.of(8, 0),LocalTime.of(15, 0))));
		rangosHorarios.put(4, List.of(new TimeInterval(LocalTime.of(8, 0),LocalTime.of(15, 0))));
		CitasFilter citasFilter =  CitasFilter.builder()
											  .semana(1)
											  .anio(2026)
											  .rangosHorarios(rangosHorarios)
											  .build();
		assertThat(citasFilter.semana()).isEqualTo(1);
		assertThat(citasFilter.anio()).isEqualTo(2026);
		assertThat(citasFilter.rangosHorarios().get(1).getFirst()).isEqualTo(new TimeInterval(LocalTime.of(8, 0),LocalTime.of(15, 0)));

	}


	@ParameterizedTest(name = "{2}")
	@MethodSource("filtrosProvider")
	void citasFilterReturnsCorrectResults(CitasFilter filtro, Predicate<List<Cita>> assertion, String descripcion) {
		List<Cita> citas = repository.getCitasByFilter(filtro);
		assertTrue(assertion.test(citas), descripcion);
	}

	static Stream<Arguments> filtrosProvider() {
		Map<Integer, List<TimeInterval>> horarioCompleto = Map.of(
			1, List.of(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(18, 0))),
			2, List.of(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(18, 0))),
			3, List.of(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(18, 0))),
			4, List.of(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(18, 0))),
			5, List.of(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(18, 0)))
		);

		return Stream.of(
			// filtros individuales
			Arguments.of(
				CitasFilter.builder().clienteId(3).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.clientId() == 3),
				"clienteId=3 solo devuelve citas del cliente 3"
			),
			Arguments.of(
				CitasFilter.builder().peluqueroId(1).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.peluqueroId() == 1),
				"peluqueroId=1 solo devuelve citas del peluquero 1"
			),
			Arguments.of(
				CitasFilter.builder().tipoServicioId(1).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.tipoServicioId() == 1),
				"tipoServicioId=1 solo devuelve citas de ese servicio"
			),
			Arguments.of(
				CitasFilter.builder().estado(EstadoCita.CONFIRMADA).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.estado() == EstadoCita.CONFIRMADA),
				"estado=CONFIRMADA solo devuelve citas confirmadas"
			),
			Arguments.of(
				CitasFilter.builder().noestado(EstadoCita.CANCELADA).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().noneMatch(c -> c.estado() == EstadoCita.CANCELADA),
				"noestado=CANCELADA no devuelve citas canceladas"
			),
			Arguments.of(
				CitasFilter.builder().semana(20).anio(2026).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c ->
					c.timestampInicio().get(WeekFields.ISO.weekOfWeekBasedYear()) == 20),
				"semana=20 solo devuelve citas de esa semana"
			),
			Arguments.of(
				CitasFilter.builder().mes(5).anio(2026).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.timestampInicio().getMonthValue() == 5),
				"mes=5 solo devuelve citas de mayo"
			),
			Arguments.of(
				CitasFilter.builder().diaSemana(2).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c -> c.timestampInicio().getDayOfWeek().getValue() == 2),
				"diaSemana=2 solo devuelve citas de martes"
			),
			Arguments.of(
				CitasFilter.builder().desde(LocalDate.of(2026, 5, 12)).hasta(LocalDate.of(2026, 5, 14)).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c ->
					!c.timestampInicio().toLocalDate().isBefore(LocalDate.of(2026, 5, 12)) &&
					!c.timestampInicio().toLocalDate().isAfter(LocalDate.of(2026, 5, 14))),
				"desde-hasta solo devuelve citas en ese rango de fechas"
			),
			Arguments.of(
				CitasFilter.builder().rangosHorarios(horarioCompleto).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c ->
					!c.timestampInicio().toLocalTime().isBefore(LocalTime.of(9, 0)) &&
					!c.timestampInicio().toLocalTime().isAfter(LocalTime.of(18, 0))),
				"rangosHorarios filtra correctamente por horario"
			),
			// combinaciones
			Arguments.of(
				CitasFilter.builder().peluqueroId(1).estado(EstadoCita.CONFIRMADA).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c ->
					c.peluqueroId() == 1 && c.estado() == EstadoCita.CONFIRMADA),
				"peluquero=1 + estado=CONFIRMADA"
			),
			Arguments.of(
				CitasFilter.builder().semana(20).anio(2026).peluqueroId(1).build(),
				(Predicate<List<Cita>>) citas -> citas.stream().allMatch(c ->
					c.peluqueroId() == 1 &&
					c.timestampInicio().get(WeekFields.ISO.weekOfWeekBasedYear()) == 20),
				"semana=20 + peluquero=1"
			),
			// caso vacío
			Arguments.of(
				CitasFilter.builder().clienteId(999).build(),
				(Predicate<List<Cita>>) List::isEmpty,
				"clienteId inexistente devuelve lista vacía"
			)
		);
	}


}