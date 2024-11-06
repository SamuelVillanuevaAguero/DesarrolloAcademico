package utilerias.busqueda;

public class filaDato {
    
    private int año;
    private String periodo;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String departamentoLicenciatura;
    private String departamentoPosgrado;
    private String acreditado;
    private int noCursos;

    public filaDato(int año, String periodo, String nombre, String apellidoPaterno, String apellidoMaterno, 
                    String departamentoLicenciatura, String departamentoPosgrado, String acreditado, int noCursos) {
        this.año = año;
        this.periodo = periodo;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.departamentoLicenciatura = departamentoLicenciatura;
        this.departamentoPosgrado = departamentoPosgrado;
        this.acreditado = acreditado;
        this.noCursos = noCursos;
    }

    public int getAño() { return año; }
    public void setAño(int año) { this.año = año; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getDepartamentoLicenciatura() { return departamentoLicenciatura; }
    public void setDepartamentoLicenciatura(String departamentoLicenciatura) { this.departamentoLicenciatura = departamentoLicenciatura; }

    public String getDepartamentoPosgrado() { return departamentoPosgrado; }
    public void setDepartamentoPosgrado(String departamentoPosgrado) { this.departamentoPosgrado = departamentoPosgrado; }

    public String getAcreditado() { return acreditado; }
    public void setAcreditado(String acreditado) { this.acreditado = acreditado; }

    public int getNoCursos() { return noCursos; }
    public void setNoCursos(int noCursos) { this.noCursos = noCursos; }
}
