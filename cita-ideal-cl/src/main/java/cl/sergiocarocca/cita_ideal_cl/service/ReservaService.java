package cl.sergiocarocca.cita_ideal_cl.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.repository.ReservaRepository;



@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva crearReserva(Reserva reserva) throws Exception {
        // 1. Validar disponibilidad
        boolean ocupado = reservaRepository.existeReservaEnEsaFecha(
                            reserva.getPlan().getId(), 
                            reserva.getFechaCita());
        
        if (ocupado) {
            throw new Exception("Lo sentimos, este horario ya no está disponible.");
        }
        
        // 2. Si está libre, guardamos
        return reservaRepository.save(reserva);
    }
    
    public List<Reserva> listarTodas() {
    	return reservaRepository.findAllByOrderByFechaCitaDesc();
    }
    public List<Reserva> guardarReservaMultiple(List<ItemCarrito> carrito, LocalDateTime fecha, Reserva datosCliente) throws Exception {
        List<Reserva> listaConfirmadas = new ArrayList<>();        
        // 1. Validar disponibilidad de TODOS los planes antes de guardar nada
        for (ItemCarrito item : carrito) {
            boolean ocupado = reservaRepository.existeReservaEnEsaFecha(
                                item.getPlan().getId(), 
                                fecha);
            
            if (ocupado) {
                // Si uno falla, lanzamos error y cancelamos todo el proceso
                throw new Exception("Lo sentimos, el servicio '" + item.getPlan().getNombre() + 
                                    "' ya no está disponible para esa fecha y hora.");
            }
        }

        // 2. Si llegamos aquí, todos están libres. Procedemos a guardar.
        for (ItemCarrito item : carrito) {
            Reserva nueva = new Reserva();
            nueva.setNombreCliente(datosCliente.getNombreCliente());
            nueva.setEmailCliente(datosCliente.getEmailCliente());
            nueva.setTelefonoCliente(datosCliente.getTelefonoCliente());
            nueva.setFechaCita(fecha);
            nueva.setPlan(item.getPlan());
            nueva.setEstado("CONFIRMADA");
            nueva.setCodigoSeguimiento(java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            
            listaConfirmadas.add(reservaRepository.save(nueva));
        }
        return listaConfirmadas;
        }
    public void eliminarReserva(Long id) throws Exception {
        // 1. Verificar si la reserva existe antes de intentar borrar
        if (!reservaRepository.existsById(id)) {
            throw new Exception("La reserva con ID " + id + " no existe.");
        }
        
        // 2. Proceder al borrado
        reservaRepository.deleteById(id);
    }
 // Agrega esto al final de tu ReservaService.java

    /**
     * Elimina una reserva por su ID.
     * @param id El identificador único de la reserva.
     * @throws Exception Si la reserva no se encuentra.
     */
    public void eliminar(Long id) throws Exception {
        reservaRepository.findById(id).ifPresentOrElse(
            reserva -> reservaRepository.delete(reserva),
            () -> {
                try {
                    throw new Exception("No se encontró la reserva para eliminar.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
