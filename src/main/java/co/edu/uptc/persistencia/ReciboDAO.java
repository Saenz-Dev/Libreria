package co.edu.uptc.persistencia;

import co.edu.uptc.modelo.ProductoCompra;
import co.edu.uptc.modelo.Recibo;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.negocio.TipoPago;

import java.sql.*;
import java.util.ArrayList;

public class ReciboDAO extends ConexionBD<Recibo>{

    @Override
    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS recibos (" +
        	"numero_producto INT AUTO_INCREMENT PRIMARY KEY, " +
                "numero_recibo INT, " +
                "correo VARCHAR(50) NOT NULL, " +
                "usuario VARCHAR(50) NOT NULL, " +
                "fecha DATETIME NOT NULL, " +
                "tipo_pago VARCHAR(20) NOT NULL, " +
                "direccion VARCHAR(100) NOT NULL, " +
                "desc.Premium DOUBLE, " +
                "desc.Frecuencia DOUBLE, " +
                "isbn BIGINT, " +
                "cantidad INT, " +
                "p.Unitario DOUBLE, " +
                "p.Total DOUBLE, " +
                "subtotal DOUBLE, " +
                "impuestos DOUBLE, " +
                "total DOUBLE, " +
                "FOREIGN KEY (correo) REFERENCES usuarios(correo), " +
                "FOREIGN KEY (usuario) REFERENCES usuarios(nombre), " +
                "FOREIGN KEY (isbn) REFERENCES libros(isbn))";

        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al crear la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void insertarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "INSERT INTO recibos (numero_recibo, correo, usuario, fecha, tipo_pago, direccion, desc.Premium, desc.Frecuencia, isbn, cantidad, p.Unitario, p.Total , subtotal, impuestos, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            preparedStatement.setString(2, recibo.getCorreo());
            preparedStatement.setString(3, recibo.getNombreUser());
            preparedStatement.setDate(4, Date.valueOf(recibo.getFecha()));
            preparedStatement.setString(5, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(6, recibo.getDireccion());
            preparedStatement.setDouble(7, recibo.getListaProductosComprados().get(0).getDescuentoPremium());
            preparedStatement.setDouble(8, recibo.getListaProductosComprados().get(0).getDescuentoFrecuencia());
            preparedStatement.setString(9, recibo.getListaProductosComprados().get(0).getIsbn());
            preparedStatement.setInt(10, recibo.getListaProductosComprados().get(0).getNumeroLibros());
            preparedStatement.setDouble(11,  recibo.getListaProductosComprados().get(0).getPrecioUnitario());
            preparedStatement.setDouble(12, recibo.getListaProductosComprados().get(0).getPrecioTotal());
            preparedStatement.setDouble(13, recibo.getValorCompra().getSubtotal());
            preparedStatement.setDouble(14, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(15, recibo.getValorCompra().getTotal());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al insertar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public void actualizarDatos(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "UPDATE recibos SET correo = ?, usuario = ?, fecha = ?, tipo_pago = ?, direccion = ?,desc.Premium = ?, desc.Frecuencia = ?, cantidad = ?, p. Unitario = ?, p.Total = ? , subtotal = ?, impuestos = ?, total = ? WHERE numero_recibo = ? AND isbn = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recibo.getCorreo());
            preparedStatement.setString(2, recibo.getNombreUser());
            preparedStatement.setDate(3, Date.valueOf(recibo.getFecha()));
            preparedStatement.setString(4, String.valueOf(recibo.getTipoPago()));
            preparedStatement.setString(5, recibo.getDireccion());
            preparedStatement.setDouble(6, recibo.getListaProductosComprados().get(0).getDescuentoPremium());
            preparedStatement.setDouble(7, recibo.getListaProductosComprados().get(0).getDescuentoFrecuencia());
            preparedStatement.setInt(8, recibo.getListaProductosComprados().get(0).getNumeroLibros());
            preparedStatement.setDouble(9,  recibo.getListaProductosComprados().get(0).getPrecioUnitario());
            preparedStatement.setDouble(10, recibo.getListaProductosComprados().get(0).getPrecioTotal());
            preparedStatement.setDouble(11, recibo.getValorCompra().getSubtotal());
            preparedStatement.setDouble(12, recibo.getValorCompra().getImpuestos());
            preparedStatement.setDouble(13, recibo.getValorCompra().getTotal());
            preparedStatement.setInt(14, recibo.getNumeroRecibo());
            preparedStatement.setString(15, recibo.getListaProductosComprados().get(0).getIsbn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("❌ Error al actualizar los datos en la tabla 'recibos': " + e.getMessage());
        }
    }

    @Override
    public Recibo seleccionarRegistro(Recibo recibo) throws SQLException, RuntimeException {
        String sql = "SELECT * FROM recibos WHERE numero_recibo = ? AND correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, recibo.getNumeroRecibo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(1));
                    reciboQuery.setCorreo(resultSet.getString(2));
                    reciboQuery.setNombreUser(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getDate(4).toString());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    return reciboQuery;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Error al seleccionar el registro en la tabla 'recibos': " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Recibo> seleccionarRegistrosCompras(Recibo recibo) throws SQLException {
        String sql = "SELECT * FROM recibos WHERE correo = ?";
        try (Connection connection = crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recibo.getCorreo());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	ArrayList<Recibo> compras = new ArrayList<>();
                while(resultSet.next()) {
                    Recibo reciboQuery = new Recibo();
                    reciboQuery.setNumeroRecibo(resultSet.getInt(1));
                    reciboQuery.setCorreo(resultSet.getString(2));
                    reciboQuery.setNombreUser(resultSet.getString(3));
                    reciboQuery.setFecha(resultSet.getDate(4).toString());
                    reciboQuery.setTipoPago(TipoPago.valueOf(resultSet.getString(5)));
                    reciboQuery.setDireccion(resultSet.getString(6));
                    ArrayList<ProductoCompra> productos = new ArrayList<>();
                    ProductoCompra productoCompra = new ProductoCompra();
                    ValorCompra valorCompra = new ValorCompra();
                    productoCompra.setDescuentoPremium(resultSet.getDouble(7));
                    productoCompra.setDescuentoFrecuencia(resultSet.getDouble(8));
                    productoCompra.setIsbn(String.valueOf(resultSet.getLong(9)));
                    productoCompra.setNumeroLibros(resultSet.getInt(10));
                    productoCompra.setPrecioUnitario(resultSet.getDouble(11));
                    productoCompra.setPrecioTotal(resultSet.getDouble(12));
                    reciboQuery.setValorCompra(valorCompra);
                    reciboQuery.getValorCompra().setSubtotal(resultSet.getDouble(13));
                    reciboQuery.getValorCompra().setImpuestos(resultSet.getDouble(14));
                    reciboQuery.getValorCompra().setTotal(resultSet.getDouble(15));
                    
                    reciboQuery.setListaProductosComprados(productos);
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
                recibo.setFecha(resultSet.getDate(4).toString());
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
