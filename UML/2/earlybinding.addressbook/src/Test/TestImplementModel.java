package Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

public class TestImplementModel {
	
	@Test
	public void implementsAddressbookFromModel() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("ecore", new XMIResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI("model/addressbook.ecore");
		Resource resource = resourceSet.createResource(fileURI);
		
		try {
			resource.load(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EPackage ePackage = (EPackage) resource.getContents().get(0);
		
		//
		//Création d'une adresse
		//
		EClass eAdresse = (EClass) ePackage.getEClassifier("Adresse");
		EAttribute eRue = (EAttribute) eAdresse.getEStructuralFeature("rue");
		EAttribute eNuméro = (EAttribute) eAdresse.getEStructuralFeature("numéro");
		
		EObject adresseInstance = ePackage.getEFactoryInstance().create(eAdresse);
		adresseInstance.eSet(eRue, "Rue des adresseInstances");
		adresseInstance.eSet(eNuméro, 5);
		
		//
		//Création des gens
		//
		EClass ePersonne = (EClass) ePackage.getEClassifier("Personne");
		EAttribute eNom = (EAttribute) ePersonne.getEStructuralFeature("Nom");
		EAttribute ePrenom = (EAttribute) ePersonne.getEStructuralFeature("Prenom");
		EAttribute eAge = (EAttribute) ePersonne.getEStructuralFeature("age");
		
		//Dupont Michel
		EObject personneInstance1 = ePackage.getEFactoryInstance().create(ePersonne);
		personneInstance1.eSet(eNom, "Dupont");
		personneInstance1.eSet(ePrenom, "Michel");
		personneInstance1.eSet(eAge, 18);
		//Dupont Jeanne
		EObject personneInstance2 = ePackage.getEFactoryInstance().create(ePersonne);
		personneInstance2.eSet(eNom, "Dupont");
		personneInstance2.eSet(ePrenom, "Jeanne");
		personneInstance2.eSet(eAge, 20);
		
		//
		// Création du carnet
		//
		EClass eAddressbook = (EClass) ePackage.getEClassifier("AdressBook");
		EReference eContains = (EReference) eAddressbook.getEStructuralFeature("personne");
		EAttribute eName = (EAttribute) eAddressbook.getEStructuralFeature("name");
		
		EObject addressbookInstance = ePackage.getEFactoryInstance().create(eAddressbook);
		addressbookInstance.eSet(eName, "Mon Carnet d'Adresses");
		List<EObject> personnes = new ArrayList<EObject>();
		personnes.add(personneInstance1);
		personnes.add(personneInstance2);
		addressbookInstance.eSet(eContains, personnes);

	}
}
