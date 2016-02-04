[![Join the chat at https://gitter.im/sebastiangoetz/slr-toolkit](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/sebastiangoetz/slr-toolkit?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![GitHub issues](https://img.shields.io/github/issues/sebastiangoetz/slr-toolkit.svg)](https://github.com/sebastiangoetz/slr-toolkit/issues) [![GitHub license](https://img.shields.io/badge/license-EPL-blue.svg)](https://raw.githubusercontent.com/sebastiangoetz/slr-toolkit/master/LICENSE) [![Build Status](https://travis-ci.org/sebastiangoetz/slr-toolkit.svg?branch=master)](https://travis-ci.org/sebastiangoetz/slr-toolkit)
# slr-toolkit

A Toolkit for Systematic Literature Reviews

### Releases

Latest release: [here](https://github.com/sebastiangoetz/slr-toolkit/releases)

### Build
From console:
* `mvn -f ./plugins/de.tudresden.slr.parent/pom.xml clean package`

Within Eclipse add Run Configurations (Run as -> Maven build):
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
