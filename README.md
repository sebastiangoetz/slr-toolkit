[![Join the chat at https://gitter.im/sebastiangoetz/slr-toolkit](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/sebastiangoetz/slr-toolkit?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![GitHub issues](https://img.shields.io/github/issues/sebastiangoetz/slr-toolkit.svg)](https://github.com/sebastiangoetz/slr-toolkit/issues) [![GitHub license](https://img.shields.io/badge/license-EPL-blue.svg)](https://raw.githubusercontent.com/sebastiangoetz/slr-toolkit/master/LICENSE) [![Build Status](https://travis-ci.org/sebastiangoetz/slr-toolkit.svg?branch=master)](https://travis-ci.org/sebastiangoetz/slr-toolkit)
# SLR Toolkit

A Toolkit for Systematic Literature Reviews

Find a short video giving an overview of the main features of the tool on [Youtube](https://youtu.be/IB4d9CJt144).

### Releases

Latest release: [here](https://github.com/sebastiangoetz/slr-toolkit/releases)

### iOS App

Since 15.02.2021 there's an iOS App of our tool developed by [Max Härtwig](https://github.com/MaxHaertwig).

All details are in the [ios-client folder](https://github.com/sebastiangoetz/slr-toolkit/tree/master/ios-client).

You can find a short video of the main features on [Youtube](https://youtu.be/P67rSa9asj8)

### Build
* From console
	* `mvn -f ./plugins/de.tudresden.slr.parent/pom.xml clean verify` for building with tests
	* `mvn -f ./plugins/de.tudresden.slr.parent/pom.xml clean package -P skipUnitAndUiTests` for building without tests
* Within Eclipse add Run Configurations (Run as -> Maven build):
	* de.tudresden.slr.target.target -> open de.tudresden.slr.target.target -> set as target platform
	* de.tudresden.slr.model.taxonomy -> de.tudresden.slr.model.taxonomy.GenerateTaxonomy.mwe2 -> Run as.. -> MWE2 Workflow
	* for plugins/de.tudresden.slr.parent/pom.xml with `clean package`
 
### Dependencies
* JDK 1.8
* Eclipse RCP 4.5.1 (EPL)
* JBibTex 1.0.15 (BSD 3-clause)
* Eclipse BIRT (EPL)
* Xtext 2.9.1 (EPL)

### License
EPL 1.0
