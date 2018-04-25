mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=%1-SNAPSHOT -f ./plugins/de.tudresden.slr.parent/pom.xml clean package
git add --all
git commit -m "Raising version number to %1"
git tag "%1"
git push --tags
git push
