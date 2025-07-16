package co.edu.uptc.negocio;

import java.util.List;

import co.edu.uptc.contrato.ICalculadoraTienda;
import co.edu.uptc.contrato.IDescuentoFrecuencia;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.Usuario;

/**
 * Clase que se encarga de realizar los cálculos de la librería como el carrito, el catálogo, los recibos.
 */
public class CalculadoraTiendaImpl implements ICalculadoraTienda, IDescuentoFrecuencia {

    private SelectorValorProducto selectorValorProducto;
    private SelectorDescuentoFrecuencia selectorDescuentoFrecuencia;

    /**
     * Constructor por defecto. Inicializa la instancia de descuentos por frecuencia.
     */
    public CalculadoraTiendaImpl(SelectorValorProducto selectorValorProducto, SelectorDescuentoFrecuencia selectorDescuentoFrecuencia) {
        this.selectorValorProducto = selectorValorProducto;
        this.selectorDescuentoFrecuencia = selectorDescuentoFrecuencia;
    }

    /**
     * Metodo que devuelve el precio total de los productos que contiene un carrito
     *
     * @param precioBase subtotal de los productos del carrito
     * @param impuestos  impuestos de los productos del carrito
     * @return suma de subtotal e impuestos
     */
    public double total(double precioBase, double impuestos) {
        return precioBase + impuestos;
    }

    /**
     * Metodo que devuelve el descuento total de un carrito
     *
     * @param precioTotal subtotal de los productos del carrito
     * @param usuario     usuario logueado
     * @return descuento total del carrito
     */
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
        return selectorValorProducto.obtenerEstrategia(libro).calcularImpuesto(libro) * libro.getStockReservado();
    }

    /**
     * Metodo que calcula el precio base de la cantidad reservada de un libro según el usuario.
     *
     * @param libro libro a calcular el precio base.
     * @return el precio base del libro por la cantidad reservada del usuario.
     */
    @Override
    public double calcularBaseTotalProducto(Libro libro) {
        return selectorValorProducto.obtenerEstrategia(libro).calcularBase(libro) * libro.getStockReservado();
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
            impuestoTotalCompra += selectorValorProducto.obtenerEstrategia(libroReservado).calcularImpuesto(libroReservado) * libroReservado.getStockReservado();
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
            baseTotalCompra += selectorValorProducto.obtenerEstrategia(libroReservado).calcularBase(libroReservado) * libroReservado.getStockReservado();
        }
        return baseTotalCompra;
    }

    /**
     * Verifica si el tipo de libro aplica a las estrategias creadas.
     *
     * @param libro el libro a consultar si aplica a alguna de las estrategias.
     * @return {@code true} si el libro aplica a alguna estrategia creada.
     */
    @Override
    public boolean aplica(Libro libro) {
        return selectorValorProducto.obtenerEstrategia(libro).aplica(libro);
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
        return selectorValorProducto.obtenerEstrategia(libro).calcularImpuesto(libro);
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
        return selectorValorProducto.obtenerEstrategia(libro).calcularBase(libro);
    }

    /**
     * Calcula el descuento según la frecuencia con que realiza
     * compras de libros.
     * @param listaRecibos lista de recibos de compra.
     * @param total total de compra para realizar el cálculo.
     * @return descuento de frecuencia de compra.
     */
    @Override
    public double calcularDescuentoFrecuencia(List<Recibo> listaRecibos, double total) {
        return selectorDescuentoFrecuencia.calcular(listaRecibos, total);
    }
}
