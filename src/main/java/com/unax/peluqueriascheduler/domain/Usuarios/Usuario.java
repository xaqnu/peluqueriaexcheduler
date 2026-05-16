package com.unax.peluqueriascheduler.domain.Usuarios;




public abstract sealed class Usuario permits Cliente, Peluquero, Admin{
    protected final int id;
    protected final String nombre;
    protected final String email;
    protected final String telefono;

    protected Usuario(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    public int id() { return id; }
    public String nombre() { return nombre; }
    public String email() { return email; }
    public String telefono() { return telefono; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nombre=" + nombre + ", email=" + email + ", telefono=" + telefono + "}";
    }
    public Cliente toCliente(){return (Cliente) this;} 
    public Peluquero toPeluquero(){return (Peluquero) this;}
    public Admin toAdmin(){return (Admin) this;}

}
    

