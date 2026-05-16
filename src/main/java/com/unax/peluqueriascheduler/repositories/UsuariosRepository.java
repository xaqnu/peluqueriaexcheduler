package com.unax.peluqueriascheduler.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static com.unax.peluqueriascheduler.generated.Tables.USUARIOS;


import com.unax.peluqueriascheduler.domain.Usuarios.Usuario;
import com.unax.peluqueriascheduler.domain.Usuarios.UsuarioRequest;
import com.unax.peluqueriascheduler.generated.tables.records.UsuariosRecord;

@Repository
public class UsuariosRepository {
    private final DSLContext dsl;

    public UsuariosRepository(DSLContext dsl) {
        this.dsl = dsl;
    }
    @Transactional(readOnly=true)
    public Usuario getUsuarioById(int id) {
        return dsl.selectFrom(USUARIOS)
                  .where(USUARIOS.ID.eq(id))
                  .fetchOneInto(Usuario.class);
    }
    @Transactional(readOnly=true)
    public Usuario getUsuarioByEmail(String email){
        return dsl.selectFrom(USUARIOS)
                  .where(USUARIOS.EMAIL.eq(email))
                  .fetchInto(Usuario.class).getFirst();

    }
    @Transactional
    public Usuario createUsuario(UsuarioRequest usuarioRequest){
        UsuariosRecord usuarioRecord=dsl.newRecord(USUARIOS,usuarioRequest);
        return dsl.insertInto(USUARIOS)
                  .set(usuarioRecord)
                  .returning()
                  .fetchOneInto(Usuario.class);
        

    }
    @Transactional
    public Usuario deleteUsuario(Usuario usuario){
        return dsl.delete(USUARIOS)
           .where(USUARIOS.ID.eq(usuario.id()))
           .returning()
           .fetchOneInto(Usuario.class);
    }
    @Transactional
    public Usuario updateCita(Usuario usuario){
        UsuariosRecord record = dsl.newRecord(USUARIOS, usuario);
        record.changed(USUARIOS.ID, false);
        
        return dsl.update(USUARIOS)
                  .set(record)
                  .where(USUARIOS.ID.eq(usuario.id()))
                  .returning()
                  .fetchOptionalInto(Usuario.class)
                  .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada: " + usuario.id()));
    }






}
