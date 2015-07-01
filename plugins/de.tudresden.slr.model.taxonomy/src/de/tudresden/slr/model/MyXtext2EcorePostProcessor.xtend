package de.tudresden.slr.model

import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage
import org.eclipse.emf.common.util.BasicEMap
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.xtext.GeneratedMetamodel
import org.eclipse.xtext.xtext.ecoreInference.IXtext2EcorePostProcessor

class MyXtext2EcorePostProcessor implements IXtext2EcorePostProcessor {

	override void process(GeneratedMetamodel metamodel) {
		metamodel.EPackage.process
	}

	def process(EPackage p) {
		for (c : p.EClassifiers.filter(typeof(EClass))) {
			if (c.name == "Term") {
				c.handle
			}
		}
	}

	def handle(EClass c) {
		val op = EcoreFactory::eINSTANCE.createEOperation
		op.name = "hashCode"
		op.EType = EcorePackage::eINSTANCE.EInt;
		val body = EcoreFactory::eINSTANCE.createEAnnotation
		body.source = GenModelPackage::eNS_URI
		val map = EcoreFactory::eINSTANCE.create(
			EcorePackage::eINSTANCE.getEStringToStringMapEntry()) as BasicEMap.Entry<String,String>
		map.key = "body"
		map.value =
		"final int prime = 31;
		int hash = 1;
		if (eContainer instanceof Term) {
			int parentHash = eContainer.hashCode();
			hash = prime * hash + parentHash ^ (parentHash >> 32);
		}
		int nameHash = name.hashCode();
		hash = prime * hash + nameHash ^ (nameHash >> 32);
		return hash;"
		body.details.add(map)
		op.EAnnotations += body
		c.EOperations += op
	}

}