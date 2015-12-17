package de.tudresden.slr.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.GeneratedMetamodel;
import org.eclipse.xtext.xtext.ecoreInference.IXtext2EcorePostProcessor;

@SuppressWarnings("all")
public class MyXtext2EcorePostProcessor implements IXtext2EcorePostProcessor {
  public void process(final GeneratedMetamodel metamodel) {
    EPackage _ePackage = metamodel.getEPackage();
    this.process(_ePackage);
  }
  
  public void process(final EPackage p) {
    EList<EClassifier> _eClassifiers = p.getEClassifiers();
    Iterable<EClass> _filter = Iterables.<EClass>filter(_eClassifiers, EClass.class);
    for (final EClass c : _filter) {
      String _name = c.getName();
      boolean _equals = Objects.equal(_name, "Term");
      if (_equals) {
        this.handle(c);
      }
    }
  }
  
  public boolean handle(final EClass c) {
    boolean _xblockexpression = false;
    {
      final EOperation op = EcoreFactory.eINSTANCE.createEOperation();
      op.setName("hashCode");
      EDataType _eInt = EcorePackage.eINSTANCE.getEInt();
      op.setEType(_eInt);
      final EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
      body.setSource(GenModelPackage.eNS_URI);
      EClass _eStringToStringMapEntry = EcorePackage.eINSTANCE.getEStringToStringMapEntry();
      EObject _create = EcoreFactory.eINSTANCE.create(_eStringToStringMapEntry);
      final BasicEMap.Entry<String, String> map = ((BasicEMap.Entry<String, String>) _create);
      map.setKey("body");
      map.setValue(
        "final int prime = 31;\r\n\t\tint hash = 1;\r\n\t\tif (eContainer instanceof Term) {\r\n\t\t\tint parentHash = eContainer.hashCode();\r\n\t\t\thash = prime * hash + parentHash ^ (parentHash >> 32);\r\n\t\t}\r\n\t\tint nameHash = name.hashCode();\r\n\t\thash = prime * hash + nameHash ^ (nameHash >> 32);\r\n\t\treturn hash;");
      EMap<String, String> _details = body.getDetails();
      _details.add(map);
      EList<EAnnotation> _eAnnotations = op.getEAnnotations();
      _eAnnotations.add(body);
      EList<EOperation> _eOperations = c.getEOperations();
      _xblockexpression = _eOperations.add(op);
    }
    return _xblockexpression;
  }
}
