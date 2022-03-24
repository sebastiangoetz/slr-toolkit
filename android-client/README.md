# SLR Toolkit App for Android

This is the SLR Toolkit App for Android. The following features are currently
supported:
- Initialize a new project by cloning a git repository that contains a SLR Toolkit project
- View publications
- View publications for different taxonomy classes
- Delete publications
- Filter publications
- Classify publications
- Analyze the current project (currently barchart and bubblechart are supported)

## Build and Run the project
In order to run the project import the project into android studio and press build and run.

## Architecture

## Project Structure

The project contains the following directories:

- database: contains everything related to persistence and database entities
- fragments: contains all the fragments
- repositories: contains all the data repositories 
- util: contains utilities used by the views
- views: contains all the views like adapters and buttons used in the fragments and activities
- viewmodels: contains all the viewmodels that are used in the fragments and activities
- worker: contains the workers

The activities are located in the root directory.

## Dependencies
The project's dependency are managed with gradle. The following dependencies are used:

- MPAndroid chart: library for visualizing data
- JGit: library used for executing all functions relating git
- JBibtex: library for parsing bibtex files
- Swipecards: library for swiping cards