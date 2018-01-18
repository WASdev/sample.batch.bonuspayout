

## what works

### 1
5. make app change, save (watch app get updated)
4. WDT run job, MFI
6. MFI, run in WDT (see new updates)

### 2
7. make app change, mvn clean install (see new updates), start server run job (see new updates)

### 3
9. server config change

### 4
9. mvn clean, delete server & runtime env (in WDT), do mvn install -dWlp=..,  Maven Update Project
	* "works" to get you back to function but uses target/wlp not -dWlp  



### observations:
* WDT doesn't run tests, WDT update doesn't rebuild mvn package (but MFI runs changed tests in spite of this)
* more of a Maven project than WDT 

## Further Reading
* [WDT in Eclipse and Maven integration](https://developer.ibm.com/wasdev/blog/2017/06/28/wdt-in-eclipse-and-maven-integration)
* [Building and running Liberty apps with Maven in Eclipse](https://developer.ibm.com/wasdev/docs/building-liberty-apps-maven-in-eclipse/) 

## Links
* Jump to [main page](/README.md)
* Back to [Maven integration](/docs/Maven-integration.md)
* Back to [Using WDT](/docs/Using-WDT.md)