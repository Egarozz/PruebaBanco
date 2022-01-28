package sernam;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Precision;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Boot {
	private double saldoAntiguo = 0.44;
	private double saldo = 20.44;
	private Movimiento mAntiguo = new Movimiento("CCDE","DEPOSITO EN EFECTIVO EN AGENTE MULTIRED","-20.00","");
	public Boot() {
		Movimiento m1 = new Movimiento("CCDE","DEPOSITO EN EFECTIVO EN AGENTE MULTIRED","-20.00","");
		Movimiento m2 = new Movimiento("CCDE","DEPOSITO EN EFECTIVO EN AGENTE MULTIRED","-20.00","");
		Movimiento m3 = new Movimiento("CCDE","DEPOSITO EN EFECTIVO EN AGENTE MULTIRED","-20.00","");
		Movimiento m4 = new Movimiento("CCRE","RETIRO EN EFECTIVO EN AGENTE MULTIRED","","20.00");
		Movimiento m5 = new Movimiento("CCDE","DEPOSITO EN EFECTIVO EN AGENTE MULTIRED","-20.00","");
		Movimiento m6 = new Movimiento("CCRE","RETIRO EN EFECTIVO EN AGENTE MULTIRED","","20.00");
		Movimiento m7 = new Movimiento("CCRE","RETIRO EN EFECTIVO EN AGENTE MULTIRED","","20.00");
		LinkedList<Movimiento> movimientos = new LinkedList<Movimiento>();
		LinkedList<Movimiento> n = new LinkedList<Movimiento>();
		movimientos.add(m7);
		movimientos.add(m6);
		movimientos.add(m5);
		movimientos.add(m4);
		movimientos.add(m3);
		movimientos.add(m2);
		movimientos.add(m1);
		
		n.add(m7);
		n.add(m7);
		n.add(m7);
		n.add(m6);
		n.add(m5);
		n.add(m4);
		n.add(m3);
		
		List<Movimiento> nuevos = getNuevoMov(movimientos, n);
		for(Movimiento m: nuevos) {
			System.out.println(m);
		}
	}
	public static void main(String[] args) {
		new Boot();
		
	}
	public List<Movimiento> getNuevoMov(LinkedList<Movimiento> m, LinkedList<Movimiento> n){
		int movimientos = 0;
		for(int i = 0; i < n.size(); i++) {
			if(arrayEqual(m,n)) {
				break;
			}
			m.pollLast();
			m.addFirst(n.get(i));
			movimientos++;
			
		}
		List<Movimiento> nuevos = new ArrayList<>();
		for(int i = 0; i < movimientos; i++) {
			nuevos.add(n.get(i));
		}
		return nuevos;
	}
	
	public boolean arrayEqual(LinkedList<Movimiento> m, LinkedList<Movimiento> n) {
		if(m.size() != n.size()) return false;
		
		for(int i = 0; i < m.size(); i++) {
			Movimiento a = m.get(i);
			Movimiento b = n.get(i);
			if(!a.codigo.equals(b.codigo)) return false;
			if(!a.descripcion.equals(b.descripcion)) return false;
			if(a.abono != b.abono) return false;
			if(a.cargo != b.cargo) return false;
		}
		return true;
	}
	public List<Movimiento> getNuevoMov(List<Movimiento> movimientos, double saldo){
		int posAntiguo = -1;
		List<Movimiento> nuevos = new ArrayList<>();
		
		for(int i = 0; i < movimientos.size(); i++) {
			Movimiento current = movimientos.get(i);
			if(current.codigo.equals(mAntiguo.codigo) && current.abono == mAntiguo.abono
					&& current.cargo == mAntiguo.cargo) {
				posAntiguo = i;
				break;
			}
		}	
		
		
	
		if(this.saldoAntiguo != saldo && (posAntiguo == 0 || posAntiguo == -1)) {
			double nuevoSaldo = saldo;
			for(int i = 0; i < movimientos.size(); i++) {
				Movimiento current = movimientos.get(i);
				nuevoSaldo += current.cargo*-1;
				nuevoSaldo = Precision.round(nuevoSaldo, 2);
				nuevoSaldo -= current.abono;
				nuevoSaldo = Precision.round(nuevoSaldo, 2);
				if(nuevoSaldo == saldoAntiguo) {
					posAntiguo = i;
					break;
				}
			}
		}	
		
		for(int i = 0; i <= posAntiguo; i++) {
			nuevos.add(movimientos.get(i));
		}
		return nuevos;
			
		
	}
	
}
