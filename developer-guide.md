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

## Setup Instructions ##

* Download Eclipse Modeling Tools (Oxygen)
* Install 
  * Xtext 
  * BIRT 
  * Maven Integration (m2e)
  * Maven Tycho Utils 
  * Tycho Project Configurators 
* Set Target Platform to .target provided in de.tudresden.slr.target
* Execute Xtext Build
  * (Project) de.tudresden.slr.model.taxonomy -> (Right-Click) src/de/tudresden/slr/model/taxonomy/GenerateTaxonomy.mwe2 -> Run As.. -> MWE2 Workflow

> Additional information can be found [here](https://github.com/sebastiangoetz/slr-toolkit/wiki/Setup-Guide).

## BibTeX Views ##

* Implemented as EMF Project: de.tudresden.slr.model.bibtex
* Bibtex Files are parsed using JBibTeX into the EMF Model
  * Model: model/bibtex.ecore
  * .bib Parsing/Pretty-Printing: src/de.tudresden.slr.model.bibtex.util/BibTexResourceImpl.java
    * doLoad(..)
    * doSave(..)
    * parseClasses(String taxonomyString) - build Taxonomy Model from string representation
    * serializeTaxonomy(..) - build string representation from Taxonomy Model
