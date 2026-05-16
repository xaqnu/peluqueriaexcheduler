package com.unax.peluqueriascheduler.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import static com.unax.peluqueriascheduler.generated.Tables.USUARIOS;
import org.jooq.Record;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;
import com.unax.peluqueriascheduler.domain.Usuarios.Usuario;
import com.unax.peluqueriascheduler.domain.Usuarios.UsuarioRequest;
import com.unax.peluqueriascheduler.generated.tables.records.UsuariosRecord;

@Repository
public class UsuariosRepository {
    private final DSLContext dsl;

    private Usuario buildUsuario(Record record) {
    int rol = record.get(USUARIOS.ROL_ID);
    int id = record.get(USUARIOS.ID);
    String nombre = record.get(USUARIOS.NOMBRE);
    String email = record.get(USUARIOS.EMAIL);
    String telefono = record.get(USUARIOS.TELEFONO);

    return switch (rol) {
        case 1-> new Cliente(id, nombre, email, telefono);
        case 2 -> new Peluquero(id, nombre, email, telefono);
        default -> throw new IllegalStateException("Rol desconocido: " + rol);
    };
}   
    public UsuariosRepository(DSLContext dsl) {
        this.dsl = dsl;
    }
    @Transactional(readOnly=true)
    public Usuario getUsuarioById(int id) {
        return buildUsuario(dsl.selectFrom(USUARIOS)
                  .where(USUARIOS.ID.eq(id))
                  .fetchOne());
    }
    @Transactional(readOnly=true)
    public Usuario getUsuarioByEmail(String email){
        return buildUsuario(dsl.selectFrom(USUARIOS)
                  .where(USUARIOS.EMAIL.eq(email))
                  .fetchOne());

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
