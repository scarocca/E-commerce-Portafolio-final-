package cl.sergiocarocca.cita_ideal_cl.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fotos")

public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String archivo; // Guardaremos el nombre: "atardecer-romantico.jpg"
    private LocalDateTime fechaCarga;
    
    
    public Foto() {
		super();
	}
    
    


	public Foto(Long id, String titulo, String archivo, LocalDateTime fechaCarga) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.archivo = archivo;
		this.fechaCarga = fechaCarga;
	}

	


	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getTitulo() {
		return titulo;
	}




	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}




	public String getArchivo() {
		return archivo;
	}




	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}




	public LocalDateTime getFechaCarga() {
		return fechaCarga;
	}




	public void setFechaCarga(LocalDateTime fechaCarga) {
		this.fechaCarga = fechaCarga;
	}




	@PrePersist
    protected void onCreate() {
        this.fechaCarga = LocalDateTime.now();
    }
}
