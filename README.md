# sample.batch.bonuspayout

This sample makes use of the ***batch-1.0*** and ***batchManagement-1.0*** features of the WebSphere Liberty Profile.
It uses the *batchManager* command-line utility to conveniently wrapper the REST-based remote dispatch and management interface.

It involves a three-step job, which generates some random data, performs a calculation on it (writing the result into the database), and then verifies the result.

Browse the code to see what it does, or build and run it yourself:

* [Building and running with Maven](/docs/Maven-integration.md)
* [Using Eclipse and WebSphere Development Tools (WDT)](/docs/Using-WDT.md)
* [Downloading WAS Liberty](/docs/Downloading-WAS-Liberty.md)
* [Run the sample manually from the command line](/docs/Running-manually-from-CLI.md)
* [Take a deeper look at the sample application](/docs/ApplicationDetails.md)
* [Organizing the project to support WDT publish and deploy within Maven build] (/docs/Using-Maven-With-WDT-Published-App.md)

## Further reading

* [A nice set of documents](https://www.ibm.com/support/techdocs/atsmastr.nsf/WebIndex/WP102544) describing Java Batch in WebSphere at various levels of detail (WP102544)
* [The sleepy batchlet sample](https://github.com/WASdev/sample.batch.sleepybatchlet), a simpler sample using just the ***batch-1.0*** feature and thus just the Java Batch 1.0 API, without the managed batch features.

## Acknowledgements

* As noted in the [maven integration](/docs/Maven-integration.md) page, this sample uses the [Liberty maven](https://github.com/WASdev/ci.maven) plugin
* Uses [ExpectIt](https://github.com/Alexey1Gavrilov/ExpectIt) for testing the ***batchManager*** command-line output

## Notice

© Copyright IBM Corporation 2015.

## License

```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
