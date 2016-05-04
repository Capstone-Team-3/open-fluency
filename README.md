### Info

Openfluency2 is a flashcard learning system for the Japanese and Chinese language. It is adapted from Openfluency by
adding new features:
 *  a Japanese dictionaries
 *  a "confuser" algorithm for generating plausible multichoice answers 
 *  multimedia flashcard import from ankiweb
 *  multimedia quiz editing, import and export
 *  deck editing
 *  student learning summary

The current release supports Japanese and Chinese flashcards.

###Installation:
1. clone this distribution.
2. Go to directory
  > cd OpenFluency
2. Install the dictionary
 * run (on mac or unix)
   * bash dictionary_setup 
 * or run (on windows)
   * exec dictionary_setup.bat
3. Start openfluency
 * grails run-app
  
###Testing:
* To run all the unit tests:
> grails test-app -unit 
  
* To run the functional tests, you need to edit Geb.config to the grails-app/conf directory, update all the text inside the < > brackets to the location of your chromedriver. A copy of chromedriver is included in the resource directory

```
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.Dimension

driver = { new ChromeDriver() }
environments {

    // run as “grails -Dgeb.env=chrome test-app”
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    chrome {
        driver = { 
            // Required for Chrome. Will need to be defined in the configuration for CI
            System.setProperty("webdriver.chrome.driver", "<Absolute path on your file system>/chromedriver");
            def driverInstance = new ChromeDriver()
            return driverInstance
        }   
    }   
    // run as “grails -Dgeb.env=firefox test-app”
    // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
    firefox {
        driver = { new FirefoxDriver() }
    }   
 }
```
This set of functional tests has been tested only on chrome, to run it 
> grails -Dgeb.env=chrome test-app
