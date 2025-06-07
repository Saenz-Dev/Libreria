package co.edu.uptc.persistencia;

import co.edu.uptc.log.RegistroLog;
import co.edu.uptc.modelo.LibroComprado;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.TipoPagoEnum;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class ReciboDAO extends ConexionBD<Recibo> {

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "INSERT INTO recibos (numero_recibo, correo, fecha, tipo_pago, direccion, descuento_Premium, descuento_Frecuencia, isbn, cantidad, precio_Unitario, precio_Total, subtotal, impuestos, total, impuesto_unitario, impuesto_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
            throw new SQLException("Ocurrió un problema al guardar el recibo. Por favor, intenta de nuevo mas tarde.");
        }
    }

    @Override
    public void actualizarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "UPDATE recibos SET correo = ?, fecha = ?, tipo_pago = ?, direccion = ?, descuento_Premium = ?, descuento_Frecuencia = ?, cantidad = ?, precio_Unitario = ?, precio_Total = ? , subtotal = ?, impuestos = ?, total = ?, impuesto_unitario = ?, impuesto_total = ?WHERE numero_recibo = ? AND isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE fecha = ? AND numero_recibo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBase(resultSet.getDouble(13));
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
            throw new SQLException("No fue posible buscar el recibo, intentalo más tarde.");
        }
    }

    public Recibo seleccionarRegistroNumero(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE numero_recibo= ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFecha()));
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Recibo reciboQuery = new Recibo();

                while (resultSet.next()) {
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
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBase(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                }
                return reciboQuery;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
        }
    }

    public ArrayList<Recibo> seleccionarRegistrosCompras(String correo) throws SQLException {
        String sql = "SELECT * FROM recibos WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correo);
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
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBase(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                    compras.add(reciboQuery);
                }
                if (!compras.isEmpty()) {
                    RegistroLog.registrarInfo("Se encontraron " + compras.size() + " recibos del usuario: " + correo);
                    return compras;
                } else {
                    RegistroLog.registrarInfo("No se encontraron recibos del usuario: " + correo);
                    return new ArrayList<>();
                }
            }
        } catch (SQLException e) {
            RegistroLog.registrarError("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage(), e);
            throw new SQLException("Ocurrió un error al buscar los recibos, intentalo más tarde.");
        }
    }

    @Override
    public ArrayList<Recibo> seleccionarRegistros() throws SQLException, RuntimeException {

        String sql = "SELECT * FROM recibos";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
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
            throw new SQLException("❌ Error al seleccionar los registros en la tabla 'recibos': " + e.getMessage());
        }
    }

    public TreeMap<String, ArrayList<Recibo>> seleccionarRecibosTienda() throws SQLException {
        String sql = "SELECT * FROM recibos";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
                    reciboQuery.getListaProductosComprados().add(libroComprado);

                    reciboQuery.getValorCompra().setPrecioBase(resultSet.getDouble(13));
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
