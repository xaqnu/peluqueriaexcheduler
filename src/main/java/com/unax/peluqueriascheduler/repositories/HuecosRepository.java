package com.unax.peluqueriascheduler.repositories;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static com.unax.peluqueriascheduler.generated.Tables.VISTA_HUECOS;

import com.unax.peluqueriascheduler.dbfilters.HuecoFilter;
import com.unax.peluqueriascheduler.domain.Huecos.Hueco;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;

@Repository
public class HuecosRepository {
    private final DSLContext dsl;

    public HuecosRepository(DSLContext dsl) {
        this.dsl = dsl;
    }
    // Este repositorio se encargará de gestionar los huecos disponibles de los peluqueros
    @Transactional(readOnly=true)
    public List<Hueco> getHuecosByPeluquero(Peluquero peluquero){
        return dsl.selectFrom(VISTA_HUECOS)
                    .where(VISTA_HUECOS.PELUQUERO_ID.eq(peluquero.id()))
                    .fetchInto(Hueco.class);
    }
    @Transactional(readOnly=true)
    public List<Hueco> getHuecosByFilter(HuecoFilter filter){
        return dsl.selectFrom(VISTA_HUECOS)
                    .where(filter.toCondition(dsl))
                    .fetchInto(Hueco.class);
    }
}