# Older instructions

This was the instruction we used when using a **beans.xml** which was an instance of the older (1.0) XSD.  No need with the newer XSD.

1. If the application cannot be targeted to the given server, it may be because WDT has not populated the project with the correct facets during import.
	1. Right-click the `batch-bonuspayout-application` project and select *Properties*.
	2. Select the *Project Facets* selection on the left-hand pane.
	3. Make sure **Java Batch** is checked ( version 1.0)
	4. Make sure **Context and dependency injection (CDI)** is checked (version 1.2)

