package co.edu.uptc.persistencia;

import co.edu.uptc.contrato.IBusquedaStrategy;
import co.edu.uptc.contrato.IConexionBD;
import co.edu.uptc.contrato.IRepositorio;
import co.edu.uptc.excepcion.RepositorioException;
import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.TipoPagoEnum;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * DAO encargado de gestionar las operaciones de persistencia relacionadas con los recibos de compra.
 * Permite insertar, actualizar, consultar y listar recibos en la base de datos.
 * Extiende la clase ConexionBD para el manejo de la conexión y operaciones genéricas.
 */
public class ReciboDAO implements IRepositorio<Recibo> {

    private IConexionBD iConexionBD;

    public ReciboDAO(IConexionBD iConexionBD) {
        this.iConexionBD = iConexionBD;
    }

    /**
     * Inserta un nuevo recibo en la base de datos, incluyendo los productos comprados.
     *
     * @param recibo objeto Recibo a insertar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void guardar(Recibo recibo) throws RepositorioException {
        String sql = "INSERT INTO recibos (numero_recibo, correo, fecha, tipo_pago, direccion, descuento_Premium, descuento_Frecuencia, isbn, cantidad, precio_Unitario, precio_Total, subtotal, impuestos, total, impuesto_unitario, impuesto_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            recibo.getFechaCompra().format(dateFormat);
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            preparedStatement.setString(2, recibo.getCorreo());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFechaCompra()));
            preparedStatement.setString(4, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(5, recibo.getDireccion());
            preparedStatement.setDouble(6, recibo.getValorCompra().getDescuentoPremium());
            preparedStatement.setDouble(7, recibo.getValorCompra().getDescuentoFrecuencia());
            preparedStatement.setDouble(12, recibo.getValorCompra().getPrecioBase());
            preparedStatement.setDouble(13, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(14, recibo.getValorCompra().getTotal());
            for (LibroComprado libroComprado : recibo.getListaProductosComprados()) {
                preparedStatement.setString(8, libroComprado.getIsbn());
                preparedStatement.setInt(9, libroComprado.getCantidadComprada());
                preparedStatement.setDouble(10, libroComprado.getPrecioVenta());
                preparedStatement.setDouble(11, libroComprado.getPrecioTotal());
                preparedStatement.setDouble(15, libroComprado.getImpuestoUnitario());
                preparedStatement.setDouble(16, libroComprado.getImpuestoTotal());
                preparedStatement.executeUpdate();
            }
            RegistroLog.registrarInfo("Se insertó correctamente el recibo num: " + recibo.getNumeroRecibo() + " del usuario: " + recibo.getCorreo());
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al insertar los datos en la tabla 'recibos': " + e.getMessage(), e);
            throw new RepositorioException("Ocurrió un problema al guardar el recibo. Por favor, intenta de nuevo mas tarde.");
        }
    }

    /**
     * Actualiza los datos de un recibo existente en la base de datos.
     *
     * @param recibo objeto Recibo a actualizar
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public void actualizar(Recibo recibo) throws RepositorioException {
        String sql = "UPDATE recibos SET correo = ?, fecha = ?, tipo_pago = ?, direccion = ?, descuento_Premium = ?, descuento_Frecuencia = ?, cantidad = ?, precio_Unitario = ?, precio_Total = ? , subtotal = ?, impuestos = ?, total = ?, impuesto_unitario = ?, impuesto_total = ? WHERE numero_recibo = ? AND isbn = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            recibo.getFechaCompra().format(dateFormat);
            preparedStatement.setString(1, recibo.getCorreo());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(recibo.getFechaCompra()));
            preparedStatement.setString(3, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(4, recibo.getDireccion());
            preparedStatement.setDouble(5, recibo.getListaProductosComprados().getFirst().getDescuentoPremium());
            preparedStatement.setDouble(6, recibo.getListaProductosComprados().getFirst().getDescuentoFrecuencia());
            preparedStatement.setInt(7, recibo.getListaProductosComprados().getFirst().getCantidadComprada());
            preparedStatement.setDouble(8, recibo.getListaProductosComprados().getFirst().getPrecioVenta());
            preparedStatement.setDouble(9, recibo.getListaProductosComprados().getFirst().getPrecioTotal());
            preparedStatement.setDouble(10, recibo.getValorCompra().getPrecioBase());
            preparedStatement.setDouble(11, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(12, recibo.getValorCompra().getTotal());
            preparedStatement.setInt(13, recibo.getNumeroRecibo());
            preparedStatement.setString(14, recibo.getListaProductosComprados().getFirst().getIsbn());
            preparedStatement.setDouble(15, recibo.getListaProductosComprados().getFirst().getImpuestoUnitario());
            preparedStatement.setDouble(16, recibo.getListaProductosComprados().getFirst().getImpuestoTotal());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al actualizar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Recibo recibo) throws RepositorioException {
        String sql = "DELETE FROM recibos WHERE correo = ? AND numero_recibo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recibo.getCorreo());
            preparedStatement.setInt(2, recibo.getNumeroRecibo());
            preparedStatement.executeUpdate();
            RegistroLog.registrarInfo("✅ Se eliminó el recibo N° " + recibo.getNumeroRecibo() + " del usuario: " + recibo.getCorreo());
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al eliminar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    /**
     * Selecciona un registro de recibo en la base de datos según el número de recibo y la fecha.
     *
     * @param recibo objeto Recibo con el número y fecha a buscar
     * @return objeto Recibo con los datos encontrados, o null si no se encuentra
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public Recibo consultar(Recibo recibo) throws RepositorioException {
        String sql = "SELECT * FROM recibos WHERE fecha = ? AND numero_recibo = ?";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFechaCompra()));
            preparedStatement.setInt(2, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Recibo reciboQuery = new Recibo();
                boolean encontrado = false;

                while (resultSet.next()) {
                    encontrado = true;
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFechaCompra(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPagoEnum.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));

                    LibroComprado libroComprado = new LibroComprado();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    libroComprado.setIsbn(String.valueOf(resultSet.getLong(9)));
                    libroComprado.setCantidadComprada(resultSet.getInt(10));
                    libroComprado.setPrecioVenta(resultSet.getDouble(11));
                    libroComprado.setPrecioTotal(resultSet.getDouble(12));
                    libroComprado.setImpuestoUnitario(resultSet.getDouble(16));
                    libroComprado.setImpuestoTotal(resultSet.getDouble(17));
                    libroComprado.setPrecioTotalSinIva(libroComprado.getCantidadComprada() * libroComprado.getPrecioVenta());
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBaseTotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                }
                if (encontrado) {
                    RegistroLog.registrarInfo("✅ Se consultó el recibo N° " + recibo.getNumeroRecibo() + " del usuario: " + recibo.getCorreo());
                    return reciboQuery;
                } else {
                    RegistroLog.registrarInfo("⚠️ No se encontró el recibo N° " + recibo.getNumeroRecibo() + " del usuario: " + recibo.getCorreo());
                    return null;
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage(), e);
            throw new RepositorioException("No fue posible buscar el recibo, intentalo más tarde.");
        }
    }

    /**
     * Selecciona todos los registros de recibos en la base de datos para un usuario específico.
     *
     * @param busquedaReciboPorCorreo ajustador de búsqueda por correo.
     * @return lista de objetos Recibo con los datos encontrados
     * @throws SQLException si ocurre un error de base de datos
     */
    public List<Recibo> consultar(IBusquedaStrategy busquedaReciboPorCorreo) throws SQLException {
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(busquedaReciboPorCorreo.getSQL())) {
            busquedaReciboPorCorreo.ajustarParametro(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<Recibo> compras = new ArrayList<>();
                while (resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFechaCompra(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPagoEnum.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));

                    LibroComprado libroComprado = new LibroComprado();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    libroComprado.setIsbn(String.valueOf(resultSet.getLong(9)));
                    libroComprado.setCantidadComprada(resultSet.getInt(10));
                    libroComprado.setPrecioVenta(resultSet.getDouble(11));
                    libroComprado.setPrecioTotal(resultSet.getDouble(12));
                    libroComprado.setImpuestoUnitario(resultSet.getDouble(16));
                    libroComprado.setImpuestoTotal(resultSet.getDouble(17));
                    libroComprado.setPrecioTotalSinIva(libroComprado.getCantidadComprada() * libroComprado.getPrecioVenta());
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBaseTotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                    compras.add(reciboQuery);
                }
                if (!compras.isEmpty()) {
                    RegistroLog.registrarInfo("Se encontraron " + compras.size() + " recibos.");
                    return compras;
                } else {
                    RegistroLog.registrarInfo("No se encontraron recibos.");
                    return new ArrayList<>();
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage(), e);
            throw new SQLException("Ocurrió un error al buscar los recibos, intentalo más tarde.");
        }
    }

    /**
     * Selecciona todos los registros de recibos en la base de datos.
     *
     * @return lista de objetos Recibo con todos los datos de recibos
     * @throws SQLException     si ocurre un error de base de datos
     * @throws RuntimeException si ocurre un error de lógica
     */
    @Override
    public List<Recibo> consultar() throws RepositorioException {
        String sql = "SELECT * FROM recibos";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            ArrayList<Recibo> recibos = new ArrayList<>();
            while (resultSet.next()) {
                Recibo recibo = new Recibo();
                recibo.setNumeroRecibo(resultSet.getInt(1));
                recibo.setCorreo(resultSet.getString(2));
                recibo.setNombreUsuario(resultSet.getString(3));
                recibo.setFechaCompra(resultSet.getTimestamp(4).toLocalDateTime());
                recibo.setTipoPago(TipoPagoEnum.valueOf(resultSet.getString(5)));
                recibo.setDireccion(resultSet.getString(6));
                recibos.add(recibo);
            }
            return recibos;
        } catch (SQLException e) {
            throw new RepositorioException("❌ Error al seleccionar los registros en la tabla 'recibos': " + e.getMessage());
        }
    }

    /**
     * Selecciona todos los recibos de la base de datos, agrupándolos por usuario.
     *
     * @return mapa con listas de recibos, donde la clave es el correo del usuario
     * @throws SQLException si ocurre un error de base de datos
     */
    public TreeMap<String, ArrayList<Recibo>> seleccionarRecibosTienda() throws SQLException {
        String sql = "SELECT * FROM recibos";
        try (Connection connection = iConexionBD.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                TreeMap<String, ArrayList<Recibo>> recibosPorUsuario = new TreeMap<>();

                while (resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFechaCompra(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPagoEnum.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));

                    LibroComprado libroComprado = new LibroComprado();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    libroComprado.setIsbn(String.valueOf(resultSet.getLong(9)));
                    libroComprado.setCantidadComprada(resultSet.getInt(10));
                    libroComprado.setPrecioVenta(resultSet.getDouble(11));
                    libroComprado.setPrecioTotal(resultSet.getDouble(12));
                    libroComprado.setImpuestoUnitario(resultSet.getDouble(16));
                    libroComprado.setImpuestoTotal(resultSet.getDouble(17));
                    libroComprado.setPrecioTotalSinIva(libroComprado.getCantidadComprada() * libroComprado.getPrecioVenta());
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBaseTotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                    if (recibosPorUsuario.containsKey(reciboQuery.getCorreo())) {
                        recibosPorUsuario.get(reciboQuery.getCorreo()).add(reciboQuery);
                    } else {
                        ArrayList<Recibo> listaRecibos = new ArrayList<>();
                        listaRecibos.add(reciboQuery);
                        recibosPorUsuario.put(reciboQuery.getCorreo(), listaRecibos);
                    }
                }
                return recibosPorUsuario;
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage(), e);
            throw new SQLException("Ocurrió un error al buscar los recibos, intentalo más tarde.");
        }
    }
}
