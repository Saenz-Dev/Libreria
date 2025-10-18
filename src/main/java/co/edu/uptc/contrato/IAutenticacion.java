package co.edu.uptc.contrato;

import co.edu.uptc.modelo.Cuenta;
import co.edu.uptc.modelo.Usuario;

public interface IAutenticacion {
    void iniciarSesion(Cuenta cuenta);

    void cerrarSesion(Cuenta cuenta);
}
