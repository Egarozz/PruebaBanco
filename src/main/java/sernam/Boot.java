package sernam;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Boot {
	private boolean p=false;
	public Boot() {
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
		driver.get("https://zonasegura1.bn.com.pe/BNWeb/Inicio");
		List<WebElement> elementos = driver.findElements(By.xpath("//div[@class='boton-clave']"));
		List<String> clave = Arrays.asList("1","4","0","6","2","6");
		for(String s: clave) {
			for(WebElement e: elementos) {
				if(e.getText().equals(s)) {
					e.click();
					try {
						Thread.sleep(1000);
					}catch(Exception ex) {}
					
				}
			}
		}
	}
	public static void main(String[] args) {
		new Boot();
		
	}
	
	
}
