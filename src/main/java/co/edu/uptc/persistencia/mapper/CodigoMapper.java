package co.edu.uptc.persistencia.mapper;

import co.edu.uptc.contrato.IMapper;
import co.edu.uptc.modelo.CodigoPremium;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CodigoMapper implements IMapper<CodigoPremium> {
    @Override
    public void mapearObjeto(CodigoPremium codigo, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, codigo.getCodigo());
        preparedStatement.setBoolean(2, codigo.getUsado());
    }

    @Override
    public CodigoPremium mapearResultSet(ResultSet resultSet) throws SQLException {
        CodigoPremium codPremium = new CodigoPremium();
        codPremium.setCodigo(resultSet.getString(1));
        codPremium.setUsado(resultSet.getBoolean(2));
        return codPremium;
    }
}
