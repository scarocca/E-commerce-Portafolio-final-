package cl.sergiocarocca.cita_ideal_cl.entity;




import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el Plan (Muchos reservas pueden pertenecer a un Plan)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    // Datos del Cliente
    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String emailCliente;

    @Column(nullable = false)
    private String telefonoCliente;

    // Fecha y Hora de la cita
    @Column(nullable = false)
    private LocalDateTime fechaCita;

    // Control de estado
    private String estado = "PENDIENTE"; // PENDIENTE, CONFIRMADA, CANCELADA
    
    private String codigoSeguimiento;

    // 1. CONSTRUCTOR VACÍO (Obligatorio para JPA)
    public Reserva() {
        // Generamos un código de seguimiento único al azar
        this.codigoSeguimiento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // 2. CONSTRUCTOR CON PARÁMETROS (Para tu comodidad)
    public Reserva(Plan plan, String nombreCliente, String emailCliente, String telefonoCliente, LocalDateTime fechaCita) {
        this(); // Llama al constructor vacío para el UUID
        this.plan = plan;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.fechaCita = fechaCita;
    }

    // 3. GETTERS Y SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public LocalDateTime getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDateTime fechaCita) { this.fechaCita = fechaCita; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }
}