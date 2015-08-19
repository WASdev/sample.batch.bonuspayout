# sample.batch.bonuspayout

This sample makes use of the ***batch-1.0*** and ***batchManagement-1.0*** features of the WebSphere Liberty Profile.
It uses the *batchManager* command-line utility to conveniently wrapper the REST-based remote dispatch and management interface.

It involves a three-step job, which generates some random data, performs a calculation on it (writing the result into the database), and then verifies the result.

Browse the code to see what it does, or build and run it yourself:

* [Building with maven](/docs/Building-the-sample.md#building-with-maven)
* [Downloading WAS Liberty](/docs/Downloading-WAS-Liberty.md)
* [Run the sample using the command line, or Maven/Gradle plugins](/docs/Running-the-sample.md)
* [Using Eclipse and WebSphere Development Tools (WDT)](/docs/Using-WDT.md)
* [Take a deeper look at the sample application](ApplicationDetails.md)
* TODO - Add Gradle build instructions

## Further reading

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
