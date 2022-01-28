package sernam;

public class Movimiento {
	public String codigo;
	public String descripcion;
	public double cargo;
	public double abono;
	public Movimiento(String codigo, String descripcion, String cargo, String abono) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cargo = cargo.equals("") ? 0 : Double.parseDouble(cargo);
		this.abono = abono.equals("") ? 0 : Double.parseDouble(abono);
	}
	public String getDinero() {
		if(cargo != 0) {
			return "Cargo: " + cargo;
		}else {
			return "Abono: " + abono;
		}
		
	}
	@Override
	public String toString() {
		return codigo + "    " + descripcion + "    " + cargo + "    " + abono;
	}
}
