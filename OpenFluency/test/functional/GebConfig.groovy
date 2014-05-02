/*
	This is the Geb configuration file.
	See: http://www.gebish.org/manual/current/configuration.html
*/

import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver

driver = { new ChromeDriver() }

environments {
	
	// run as "grails -Dgeb.env=chrome test-app functional:"
	// See: http://code.google.com/p/selenium/wiki/ChromeDriver
	chrome {
		driver = { 
			// Required for Chrome. Will need to be defined in the configuration for CI
			// System.setProperty("webdriver.chrome.driver", "/Applications/ChromeDriver/chromedriver");
			// Windows:
			System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
			def driverInstance = new ChromeDriver() 
			driverInstance.manage().window().maximize() 
			driverInstance
		}
	}
	
	// run as grails -Dgeb.env=firefox test-app
	// See: http://code.google.com/p/selenium/wiki/FirefoxDriver
	firefox {
		driver = { new FirefoxDriver() }
	}

}