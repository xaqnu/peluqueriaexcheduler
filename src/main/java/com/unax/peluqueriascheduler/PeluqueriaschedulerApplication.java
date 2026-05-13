package com.unax.peluqueriascheduler;

import org.jooq.DSLContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

 
import static com.unax.peluqueriascheduler.generated.Tables.VISTA_HUECOS;

@SpringBootApplication
public class PeluqueriaschedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaschedulerApplication.class, args);
	}
	@Bean
    CommandLineRunner checkTables(DSLContext dsl) {
        return args -> {
            var tables = dsl.meta().getTables();
			
            tables.stream()
      			  .filter(t -> t.getSchema().getName().equals("public"))
                  .forEach(t -> System.out.println("Tabla: " + t.getName()));
            var huecos=dsl.selectFrom(VISTA_HUECOS)
               .where(VISTA_HUECOS.PELUQUERO_ID.eq(1))
               .orderBy(VISTA_HUECOS.DESDE)
               .fetch();
                huecos.forEach(h -> System.out.println("Hueco: " + h.getDesde() + " - " + h.getHasta()));
        };
    }

}
