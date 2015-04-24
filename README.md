[![Join the chat at https://gitter.im/sebastiangoetz/slr-toolkit](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/sebastiangoetz/slr-toolkit?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![GitHub issues](https://img.shields.io/github/issues/sebastiangoetz/slr-toolkit.svg)](https://github.com/sebastiangoetz/slr-toolkit/issues) [![GitHub license](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://raw.githubusercontent.com/sebastiangoetz/slr-toolkit/master/LICENSE)
# slr-toolkit

A Toolkit for Systematic Literature Reviews

### Build
From console:
* `sh -c "mvn -f ./repositories/de.tudresden.slr.thirdparty/pom.xml p2:site jetty:run &"`
* `mvn -f ./plugins/de.tudresden.slr.parent/pom.xml clean package`

Within Eclipse add Run Configurations (Run as -> Maven build):
* for repositories/de.tudresden.slr.thirdparty/pom.xml with `p2:site jetty:run`
* for plugins/de.tudresden.slr.parent/pom.xml with `clean package`

### Dependencies
* Eclipse RCP (EPL)
* JBibTex (BSD 3-clause)
* iText (AGPL)

### License
GPLv3

