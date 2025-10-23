package co.edu.uptc.negocio;

import java.util.List;

import co.edu.uptc.contrato.ICalculadoraTienda;
import co.edu.uptc.contrato.IDescuentoFrecuencia;
import co.edu.uptc.contrato.IValorProducto;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Usuario;

/**
 * Clase que se encarga de realizar los cálculos de la librería como el carrito, el catálogo, los recibos.
 */
public class CalculadoraTiendaImpl implements ICalculadoraTienda {

    private IDescuentoFrecuencia iDescuentoFrecuencia;
    private IValorProducto iValorProducto;

    /**
     * Constructor por defecto. Inicializa la instancia de descuentos por frecuencia.
     */
    public CalculadoraTiendaImpl(IValorProducto iValorProducto, IDescuentoFrecuencia iDescuentoFrecuencia) {
        this.iValorProducto = iValorProducto;
        this.iDescuentoFrecuencia = iDescuentoFrecuencia;
    }

    /**
     * Metodo que devuelve el precio total de los productos que contiene un carrito
     *
     * @param precioBase subtotal de los productos del carrito
     * @param impuestos  impuestos de los productos del carrito
     * @return suma de subtotal e impuestos
     */
    @Override
    public double calcularTotal(double precioBase, double impuestos) {
        return precioBase + impuestos;
    }

    /**
     * Metodo que devuelve el descuento total de un carrito
     *
     * @param precioTotal subtotal de los productos del carrito
     * @param usuario     usuario logueado
     * @return descuento total del carrito
     */
    @Override
    public double descuentoPremiumTotal(double precioTotal, Usuario usuario) {
        return precioTotal * usuario.getDescuentoTipoUsuario();
    }

    /**
     * Metodo que devuelve el impuesto total de la cantidad de cada libro.
     *
     * @param libro el libro a calcular el impuesto.
     * @return impuesto del libro por la cantidad reservada.
     */
    @Override
    public double calcularImpuestoTotalProducto(Libro libro) {
        return iValorProducto.calcularImpuesto(libro) * libro.getStockReservado();
    }

    /**
     * Metodo que calcula el precio base de la cantidad reservada de un libro según el usuario.
     *
     * @param libro libro a calcular el precio base.
     * @return el precio base del libro por la cantidad reservada del usuario.
     */
    @Override
    public double calcularBaseTotalProducto(Libro libro) {
        return iValorProducto.calcularBase(libro) * libro.getStockReservado();
    }

    /**
     * Calcula el IVA total de la compra según la lógica ligada
     * a cada tipo de estrategia correspondiente al tipo de libro.
     *
     * @param librosReservados la lista de libros reservados para realizar la compra.
     * @return el precio total del IVA de la lista de compra.
     */
    @Override
    public double calcularImpuestoTotalCompra(List<Libro> librosReservados) {
        double impuestoTotalCompra = 0;
        for (Libro libroReservado : librosReservados) {
            impuestoTotalCompra += iValorProducto.calcularImpuesto(libroReservado) * libroReservado.getStockReservado();
        }
        return impuestoTotalCompra;
    }

    /**
     * Calcula el precio base total (sin impuestos) de la compra,
     * según la lógica enlazada a cada tipo de estrategia correspondiente al tipo de libro.
     *
     * @param librosReservados la lista de libros a los que se desea calcular.
     * @return el precio base total de la lista de compra.
     */
    @Override
    public double calcularBaseTotalCompra(List<Libro> librosReservados) {
        double baseTotalCompra = 0;
        for (Libro libroReservado : librosReservados) {
            baseTotalCompra += iValorProducto.calcularBase(libroReservado) * libroReservado.getStockReservado();
        }
        return baseTotalCompra;
    }

    /**
     * Calcula el impuesto de un libro, dependiendo de la lógica al tipo específico de estrategia
     * correspondiente al tipo de libro.
     *
     * @param libro el libro a calcular el impuesto unitario.
     * @return el impuesto calculado del libro.
     */
    @Override
    public double calcularImpuesto(Libro libro) {
        return iValorProducto.calcularImpuesto(libro);
    }

    /**
     * Calcula el precio base (sin impuestos) de un libro,
     * delegando la lógica al tipo específico de estrategia
     * correspondiente al tipo de libro (físico o digital).
     *
     * @param libro el libro cuyo precio base se desea calcular.
     * @return el precio base unitario del libro, excluyendo impuestos.
     * @throws IllegalArgumentException si el tipo de libro no está soportado
     *                                  o no se encuentra estrategia asociada.
     */
    @Override
    public double calcularBase(Libro libro) {
        return iValorProducto.calcularBase(libro);
    }

    /**
     * Calcula el descuento según la frecuencia con que realiza
     * compras de libros.
     *
     * @param listaRecibos lista de recibos de compra.
     * @param total        total de compra para realizar el cálculo.
     * @return descuento de frecuencia de compra.
     */
    @Override
    public double calcularDescuentoContinuidad(List<Recibo> listaRecibos, double total) {
        double descuento = 0;
        iDescuentoFrecuencia = new DescuentoFrecuenciaDiez();
        descuento += iDescuentoFrecuencia.calcularDescuentoFrecuencia(listaRecibos, total);
        iDescuentoFrecuencia = new DescuentoFrecuenciaVeinte();
        descuento += iDescuentoFrecuencia.calcularDescuentoFrecuencia(listaRecibos, total);
        iDescuentoFrecuencia = new DescuentoFrecuenciaCincuenta();
        descuento += iDescuentoFrecuencia.calcularDescuentoFrecuencia(listaRecibos, total);
        return descuento;
    }
}
