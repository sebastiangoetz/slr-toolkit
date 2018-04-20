# Developer's Guide #

## Project Structure ##

* de.tudresden.slr.parent - main project, start maven build from here (clean package)
* RCP Infrastructure Projects
  * de.tudresden.slr.target - Eclipse Target Site 
  * de.tudresden.slr.app - Eclipse RCP Application and Perspective
  * de.tudresden.slr.app.feature - Main Feature (aggregating all slr-toolkit plugins)
  * de.tudresden.slr.product - Eclipse Product Definition
* Central SLR Toolkit Plugins
  * de.tudresden.slr.model.bibtex (EMF Model for BibTex Entries + generated subprojects)
  * de.tudresden.slr.model.taxonomy (Xtext Language for Taxonomy + generated subprojects)
  * de.tudresden.slr.model.modelregistry
* Special SLR Toolkit Plugins
  * de.tudresden.slr.googlescholar - Google Scholar Import
  * de.tudresden.slr.model.mendeley - Mendeley Synchronization
  * de.tudresden.slr.ui.charts - BIRT-based Charting
  * de.tudresden.slr.wizards - Wizards for SLR Projects
