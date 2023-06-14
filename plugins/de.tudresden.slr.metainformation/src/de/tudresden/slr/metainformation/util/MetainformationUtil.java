package de.tudresden.slr.metainformation.util;

import java.io.File;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.sun.xml.bind.v2.ContextFactory;
//import com.thoughtworks.xstream;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;

public class MetainformationUtil {
	/**
	 * Unmarshals a xml-file to a metainformation object
	 * @param file XML File
	 * @return Metainformation object with information from specified file, if it fit the schema of
	 * SlrProjectMetainformation
	 * @throws JAXBException
	 */
	public static SlrProjectMetainformation getMetainformationFromFile(File file) throws JAXBException {		
		JAXBContext jaxbContext = //JAXBContext.newInstance(SlrProjectMetainformation.class);
				ContextFactory.createContext(new Class[] { SlrProjectMetainformation.class }, Collections.<String, Object>emptyMap());

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		SlrProjectMetainformation metainformation = (SlrProjectMetainformation) jaxbUnmarshaller.unmarshal(file);
		return metainformation;
	}
}
