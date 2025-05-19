package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.negocio.TipoPago;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;

public class ReciboDAO extends ConexionBD<Recibo>{

    @Override
    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS recibos (" +
        	"numero_producto INT AUTO_INCREMENT PRIMARY KEY, " +
                "numero_recibo INT, " +
                "correo VARCHAR(50) NOT NULL, " +
                "fecha DATETIME NOT NULL, " +
                "tipo_pago VARCHAR(20) NOT NULL, " +
                "direccion VARCHAR(100) NOT NULL, " +
                "descuento_Premium DOUBLE, " +
                "descuento_Frecuencia DOUBLE, " +
                "isbn BIGINT, " +
                "cantidad INT, " +
                "precio_Unitario DOUBLE, " +
                "precio_Total DOUBLE, " +
                "subtotal DOUBLE, " +
                "impuestos DOUBLE, " +
                "total DOUBLE, " +
                "FOREIGN KEY (numero_recibo) REFERENCES compras(numero_compra), " +
                "FOREIGN KEY (correo) REFERENCES usuarios(correo), " +
                "FOREIGN KEY (isbn) REFERENCES libros(isbn))";

        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "INSERT INTO recibos (numero_recibo, correo, fecha, tipo_pago, direccion, descuento_Premium, descuento_Frecuencia, isbn, cantidad, precio_Unitario, precio_Total, subtotal, impuestos, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            recibo.getFecha().format(dateFormat);
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            preparedStatement.setString(2, recibo.getCorreo());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(recibo.getFecha()));
            preparedStatement.setString(4, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(5, recibo.getDireccion());
            preparedStatement.setDouble(6, recibo.getValorCompra().getDescuentoPremium());
            preparedStatement.setDouble(7, recibo.getValorCompra().getDescuentoFrecuencia());
            preparedStatement.setString(8, recibo.getListaProductosComprados().get(0).getIsbn());
            preparedStatement.setInt(9, recibo.getListaProductosComprados().get(0).getNumeroLibros());
            preparedStatement.setDouble(10,  recibo.getListaProductosComprados().get(0).getPrecioUnitario());
            preparedStatement.setDouble(11, recibo.getListaProductosComprados().get(0).getPrecioTotal());
            preparedStatement.setDouble(12, recibo.getValorCompra().getSubtotal());
            preparedStatement.setDouble(13, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(14, recibo.getValorCompra().getTotal());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "UPDATE recibos SET correo = ?, fecha = ?, tipo_pago = ?, direccion = ?, descuento_Premium = ?, descuento_Frecuencia = ?, cantidad = ?, precio_Unitario = ?, precio_Total = ? , subtotal = ?, impuestos = ?, total = ? WHERE numero_recibo = ? AND isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            recibo.getFecha().format(dateFormat);
            preparedStatement.setString(1, recibo.getCorreo());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(recibo.getFecha()));
            preparedStatement.setString(3, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(4, recibo.getDireccion());
            preparedStatement.setDouble(5, recibo.getListaProductosComprados().get(0).getDescuentoPremium());
            preparedStatement.setDouble(6, recibo.getListaProductosComprados().get(0).getDescuentoFrecuencia());
            preparedStatement.setInt(7, recibo.getListaProductosComprados().get(0).getNumeroLibros());
            preparedStatement.setDouble(8,  recibo.getListaProductosComprados().get(0).getPrecioUnitario());
            preparedStatement.setDouble(9, recibo.getListaProductosComprados().get(0).getPrecioTotal());
            preparedStatement.setDouble(10, recibo.getValorCompra().getSubtotal());
            preparedStatement.setDouble(11, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(12, recibo.getValorCompra().getTotal());
            preparedStatement.setInt(13, recibo.getNumeroRecibo());
            preparedStatement.setString(14, recibo.getListaProductosComprados().get(0).getIsbn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE fecha = ? AND numero_recibo= ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFecha()));
            preparedStatement.setInt(2, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	Recibo reciboQuery = new Recibo();
        	
                while(resultSet.next()) {
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    
                    ProductoCompra productoCompra = new ProductoCompra();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    productoCompra.setIsbn(String.valueOf(resultSet.getLong(9)));
                    productoCompra.setNumeroLibros(resultSet.getInt(10));
                    productoCompra.setPrecioUnitario(resultSet.getDouble(11));
                    productoCompra.setPrecioTotal(resultSet.getDouble(12));
                    reciboQuery.getListaProductosComprados().add(productoCompra);
                    
                    reciboQuery.getValorCompra().setSubtotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));                    
                }
                return reciboQuery;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
        }
    }
    
    public Recibo seleccionarRegistroNumero(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE numero_recibo= ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //preparedStatement.setTimestamp(1, Timestamp.valueOf(recibo.getFecha()));
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	Recibo reciboQuery = new Recibo();
        	
                while(resultSet.next()) {
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    
                    ProductoCompra productoCompra = new ProductoCompra();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    productoCompra.setIsbn(String.valueOf(resultSet.getLong(9)));
                    productoCompra.setNumeroLibros(resultSet.getInt(10));
                    productoCompra.setPrecioUnitario(resultSet.getDouble(11));
                    productoCompra.setPrecioTotal(resultSet.getDouble(12));
                    reciboQuery.getListaProductosComprados().add(productoCompra);
                    
                    reciboQuery.getValorCompra().setSubtotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));                    
                }
                return reciboQuery;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
        }
    }
    
    public ArrayList<Recibo> seleccionarRegistrosCompras(Recibo recibo) throws SQLException {
        String sql = "SELECT * FROM recibos WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recibo.getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	ArrayList<Recibo> compras = new ArrayList<>();
        	DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                while(resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(2));
                    reciboQuery.setCorreo(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getTimestamp(4).toLocalDateTime());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    
                    ProductoCompra productoCompra = new ProductoCompra();
                    reciboQuery.getValorCompra().setDescuentoPremium(resultSet.getDouble(7));
                    reciboQuery.getValorCompra().setDescuentoFrecuencia(resultSet.getDouble(8));
                    productoCompra.setIsbn(String.valueOf(resultSet.getLong(9)));
                    productoCompra.setNumeroLibros(resultSet.getInt(10));
                    productoCompra.setPrecioUnitario(resultSet.getDouble(11));
                    productoCompra.setPrecioTotal(resultSet.getDouble(12));
                    reciboQuery.getListaProductosComprados().add(productoCompra);
                    
                    reciboQuery.getValorCompra().setSubtotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15)); 
                    compras.add(reciboQuery);
                }
                return compras;
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
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
                recibo.setNombreUser(resultSet.getString(3));
                recibo.setFecha(resultSet.getTimestamp(4).toLocalDateTime());
                recibo.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                recibo.setDireccion(resultSet.getString(6));
                recibos.add(recibo);
            }
            return recibos;
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar los registros en la tabla 'recibos': " + e.getMessage());
        }
    }
}
