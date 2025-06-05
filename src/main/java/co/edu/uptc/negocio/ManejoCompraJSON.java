package co.edu.uptc.negocio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import co.edu.uptc.modelo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ManejoCompraJSON {

    private File file;

    private ObjectMapper objectMapper;

    private String ruta;

    private Tienda tienda;

    public Tienda getTienda() {
        return tienda;
    }

    public ManejoCompraJSON(Tienda tienda) {
        ruta = "src/main/java/co/edu/uptc/persistencia/compra.json";

        file = new File(ruta);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.tienda = tienda;
    }

    public void leerCompras() throws IOException {
        if (file.length() == 0) {
            tienda.setRecibosTienda(new TreeMap<>());
            objectMapper.writeValue(file, tienda.getRecibosTienda());
        }
        tienda.setRecibosTienda(objectMapper.readValue(file, new TypeReference<TreeMap<String, ArrayList<Recibo>>>() {}));
    }

    public void crearCompra(Recibo recibo, Usuario usuarioLog) {
        try {
            if (!file.exists()) file.createNewFile();
            leerCompras();
            if (tienda.getRecibosTienda().get(usuarioLog.getCuenta().getCorreo()) == null) {
                ArrayList<Recibo> listaRecibos = new ArrayList<>();
                recibo.setNumeroRecibo(numeroFactura());
                listaRecibos.add(recibo);
                tienda.getRecibosTienda().put(usuarioLog.getCuenta().getCorreo(), listaRecibos);
                objectMapper.writeValue(file, tienda.getRecibosTienda());
                return;
            }
            recibo.setNumeroRecibo(numeroFactura());
            tienda.getRecibosTienda().get(usuarioLog.getCuenta().getCorreo()).add(recibo);

            objectMapper.writeValue(file, tienda.getRecibosTienda());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int numeroFactura() throws IOException {
        leerCompras();
        int numero = 0;
        for (ArrayList<Recibo> listaRecibos: getTienda().getRecibosTienda().values()) {
            for (Recibo p : listaRecibos) {
                for (LibroComprado compra : p.getListaProductosComprados()) {
                    numero++;
                }
            }
        }
        return numero++;
    }
}
