package de.tudresden.slr.model.bibtex.tests;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.util.BibtexTooling;

public class BibtexToolingTest {

	@Test
	public void mergeEqualDatabases() throws TokenMgrException, ParseException {
		String bib = "@article{lee2016ontology,\r\n" + 
				"  title={An ontology-based approach for developing data exchange requirements and model views of building information modeling},\r\n" + 
				"  author={Lee, Yong-Cheol and Eastman, Charles M and Solihin, Wawan},\r\n" + 
				"  journal={Advanced Engineering Informatics},\r\n" + 
				"  volume={30},\r\n" + 
				"  number={3},\r\n" + 
				"  pages={354--367},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S1474034616300738}},\r\n" + 
				"  citations={14}\r\n" + 
				"}\r\n" + 
				"@article{belsky2016semantic,\r\n" + 
				"  title={Semantic enrichment for building information modeling},\r\n" + 
				"  author={Belsky, Michael and Sacks, Rafael and Brilakis, Ioannis},\r\n" + 
				"  journal={Computer-Aided Civil and Infrastructure Engineering},\r\n" + 
				"  volume={31},\r\n" + 
				"  number={4},\r\n" + 
				"  pages={261--274},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Wiley Online Library},\r\n" + 
				"  howpublished = {\\url{http://onlinelibrary.wiley.com/doi/10.1111/mice.12128/full}},\r\n" + 
				"  citations={27}\r\n" + 
				"}\r\n" + 
				"@article{motamedi2016extending,\r\n" + 
				"  title={Extending IFC to incorporate information of RFID tags attached to building elements},\r\n" + 
				"  author={Motamedi, Ali and Soltani, Mohammad Mostafa and Setayeshgar, Shayan and Hammad, Amin},\r\n" + 
				"  journal={Advanced Engineering Informatics},\r\n" + 
				"  volume={30},\r\n" + 
				"  number={1},\r\n" + 
				"  pages={39--53},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S1474034615001287}},\r\n" + 
				"  citations={19}\r\n" + 
				"}\r\n" + 
				"@article{jeong2016algorithm,\r\n" + 
				"  title={An algorithm to translate building topology in building information modeling into Object-Oriented physical modeling-based building energy modeling},\r\n" + 
				"  author={Jeong, WoonSeong and Son, JeongWook},\r\n" + 
				"  journal={Energies},\r\n" + 
				"  volume={9},\r\n" + 
				"  number={1},\r\n" + 
				"  pages={50},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Multidisciplinary Digital Publishing Institute},\r\n" + 
				"  howpublished = {\\url{http://www.mdpi.com/1996-1073/9/1/50}},\r\n" + 
				"  citations={4}\r\n" + 
				"}\r\n" + 
				"@article{barazzetti2016parametric,\r\n" + 
				"  title={Parametric as-built model generation of complex shapes from point clouds},\r\n" + 
				"  author={Barazzetti, Luigi},\r\n" + 
				"  journal={Advanced Engineering Informatics},\r\n" + 
				"  volume={30},\r\n" + 
				"  number={3},\r\n" + 
				"  pages={298--311},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S1474034616300477}},\r\n" + 
				"  citations={15}\r\n" + 
				"}\r\n" + 
				"@article{liu2016ontology,\r\n" + 
				"  title={Ontology-based semantic approach for construction-oriented quantity take-off from BIM models in the light-frame building industry},\r\n" + 
				"  author={Liu, Hexu and Lu, Ming and Al-Hussein, Mohamed},\r\n" + 
				"  journal={Advanced Engineering Informatics},\r\n" + 
				"  volume={30},\r\n" + 
				"  number={2},\r\n" + 
				"  pages={190--207},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S1474034616300246}},\r\n" + 
				"  citations={10}\r\n" + 
				"}\r\n" + 
				"@article{golabchi2016automated,\r\n" + 
				"  title={Automated building information modeling for fault detection and diagnostics in commercial HVAC systems},\r\n" + 
				"  author={Golabchi, Alireza and Akula, Manu and Kamat, Vineet},\r\n" + 
				"  journal={Facilities},\r\n" + 
				"  volume={34},\r\n" + 
				"  number={3/4},\r\n" + 
				"  pages={233--246},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Emerald Group Publishing Limited},\r\n" + 
				"  howpublished = {\\url{http://www.emeraldinsight.com/doi/abs/10.1108/F-06-2014-0050}},\r\n" + 
				"  citations={3}\r\n" + 
				"}\r\n" + 
				"@article{ding2016construction,\r\n" + 
				"  title={Construction risk knowledge management in BIM using ontology and semantic web technology},\r\n" + 
				"  author={Ding, LY and Zhong, BT and Wu, Song and Luo, HB},\r\n" + 
				"  journal={Safety science},\r\n" + 
				"  volume={87},\r\n" + 
				"  pages={202--213},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S0925753516300492}},\r\n" + 
				"  citations={15}\r\n" + 
				"}\r\n" + 
				"@inproceedings{pauwels2016simplebim,\r\n" + 
				"  title={SimpleBIM: From full ifcOWL graphs to simplified building graphs},\r\n" + 
				"  author={Pauwels, Pieter and Roxin, Anna},\r\n" + 
				"  booktitle={Proceedings of the 11th European Conference on Product and Process Modelling (ECPPM)},\r\n" + 
				"  pages={11--18},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{https://content.taylorfrancis.com/books/download?dac=C2016-0-25202-4&isbn=9781315386898&format=googlePreviewPdf#page=25}},\r\n" + 
				"  citations={7}\r\n" + 
				"}\r\n" + 
				"@inproceedings{goccer2016bim,\r\n" + 
				"  title={A BIM-GIS integrated pre-retrofit model for building data mapping},\r\n" + 
				"  author={G{\\\"o}{\\c{c}}er, {\\\"O}zg{\\\"u}r and Hua, Ying and G{\\\"o}{\\c{c}}er, Kenan},\r\n" + 
				"  booktitle={Building Simulation},\r\n" + 
				"  volume={9},\r\n" + 
				"  number={5},\r\n" + 
				"  pages={513--527},\r\n" + 
				"  year={2016},\r\n" + 
				"  organization={Springer},\r\n" + 
				"  howpublished = {\\url{https://link.springer.com/article/10.1007/s12273-016-0293-4}},\r\n" + 
				"  citations={6}\r\n" + 
				"}\r\n" + 
				"@article{pinheiro2016model,\r\n" + 
				"  title={Model view definition for advanced building energy performance simulation},\r\n" + 
				"  author={Pinheiro, S and Donnell, JO and Wimmer, R and Bazjanac, V and Muhic, S and Maile, Z and Frisch, J and Van Treeck, C},\r\n" + 
				"  journal={Proceeding of the CESBP Central European Symbosium on Building Physics and BauSIM 2016},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{https://www.researchgate.net/profile/Sergio_Pinheiro4/publication/308614558_Model_View_Definition_for_Advanced_Building_Energy_Performance_Simulation/links/57e8e5b308aed7fe466bf455.pdf}},\r\n" + 
				"  citations={8}\r\n" + 
				"}\r\n" + 
				"@article{lee2016linked,\r\n" + 
				"  title={A linked data system framework for sharing construction defect information using ontologies and BIM environments},\r\n" + 
				"  author={Lee, Do-Yeop and Chi, Hung-lin and Wang, Jun and Wang, Xiangyu and Park, Chan-Sik},\r\n" + 
				"  journal={Automation in Construction},\r\n" + 
				"  volume={68},\r\n" + 
				"  pages={102--113},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S0926580516300838}},\r\n" + 
				"  citations={9}\r\n" + 
				"}\r\n" + 
				"@article{liu2016approach,\r\n" + 
				"  title={An approach to 3D model fusion in GIS systems and its application in a future ECDIS},\r\n" + 
				"  author={Liu, Tao and Zhao, Depeng and Pan, Mingyang},\r\n" + 
				"  journal={Computers \\& Geosciences},\r\n" + 
				"  volume={89},\r\n" + 
				"  pages={12--20},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Elsevier},\r\n" + 
				"  howpublished = {\\url{https://www.sciencedirect.com/science/article/pii/S0098300416300085}},\r\n" + 
				"  citations={7}\r\n" + 
				"}\r\n" + 
				"@article{lehtomaki2016object,\r\n" + 
				"  title={Object classification and recognition from mobile laser scanning point clouds in a road environment},\r\n" + 
				"  author={Lehtom{\\\"a}ki, Matti and Jaakkola, Anttoni and Hyypp{\\\"a}, Juha and Lampinen, Jouko and Kaartinen, Harri and Kukko, Antero and Puttonen, Eetu and Hyypp{\\\"a}, Hannu},\r\n" + 
				"  journal={IEEE Transactions on Geoscience and Remote Sensing},\r\n" + 
				"  volume={54},\r\n" + 
				"  number={2},\r\n" + 
				"  pages={1226--1239},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={IEEE},\r\n" + 
				"  howpublished = {\\url{http://ieeexplore.ieee.org/abstract/document/7287763/}},\r\n" + 
				"  citations={20}\r\n" + 
				"}\r\n" + 
				"@article{zou2016developing,\r\n" + 
				"  title={Developing a tailored RBS linking to BIM for risk management of bridge projects},\r\n" + 
				"  author={Zou, Yang and Kiviniemi, Arto and Jones, Stephen W},\r\n" + 
				"  journal={Engineering, Construction and Architectural Management},\r\n" + 
				"  volume={23},\r\n" + 
				"  number={6},\r\n" + 
				"  pages={727--750},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Emerald Group Publishing Limited},\r\n" + 
				"  howpublished = {\\url{http://www.emeraldinsight.com/doi/abs/10.1108/ECAM-01-2016-0009}},\r\n" + 
				"  citations={6}\r\n" + 
				"}\r\n" + 
				"@inproceedings{schwartz2016semantically,\r\n" + 
				"  title={Semantically enriched BIM Life Cycle Assessment to enhance buildings    environmental performance},\r\n" + 
				"  author={Schwartz, YAIR and Eleftheriadis, STATHIS and Raslan, ROKIA and Mumovic, DEJAN},\r\n" + 
				"  booktitle={Proceedings of the CIBSE Technical Symposium. Edinburgh, UK},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{https://www.researchgate.net/profile/Stathis_Eleftheriadis/publication/296949281_Semantically_Enriched_BIM_Life_Cycle_Assessment_to_Enhance_Buildings'_Environmental_Performance/links/571ddf2208ae7f552a4a7dd7.pdf}},\r\n" + 
				"  citations={3}\r\n" + 
				"}\r\n" + 
				"@inproceedings{valdes2016applying,\r\n" + 
				"  title={Applying Systems Modeling Approaches to Building Construction},\r\n" + 
				"  author={Valdes, Francisco and Gentry, Russell and Eastman, Charles and Forrest, Stephen},\r\n" + 
				"  booktitle={ISARC. Proceedings of the International Symposium on Automation and Robotics in Construction},\r\n" + 
				"  volume={33},\r\n" + 
				"  pages={1},\r\n" + 
				"  year={2016},\r\n" + 
				"  organization={Vilnius Gediminas Technical University, Department of Construction Economics \\& Property},\r\n" + 
				"  howpublished = {\\url{http://search.proquest.com/openview/a1250dd9d37467900b786eae9ea1a893/1?pq-origsite=gscholar&cbl=1646340}},\r\n" + 
				"  citations={1}\r\n" + 
				"}\r\n" + 
				"@inproceedings{miyake2016outdoor,\r\n" + 
				"  title={Outdoor augmented reality using optical see-through HMD system for visualizing building information},\r\n" + 
				"  author={Miyake, Munetoshi and Fukuda, Tomohiro and Yabuki, Nobuyohsi and Motamedi, Ali and Michikawa, Takashi},\r\n" + 
				"  booktitle={16th International Conference on Computing in Civil and Building Engineering},\r\n" + 
				"  pages={1644--1651},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{https://www.researchgate.net/profile/Tomohiro_Fukuda2/publication/305046550_Outdoor_Augmented_Reality_using_Optical_see-through_HMD_System_for_Visualizing_Building_Information/links/57804c2208ae5f367d37151d/Outdoor-Augmented-Reality-using-Optical-see-through-HMD-System-for-Visualizing-Building-Information.pdf}},\r\n" + 
				"  citations={1}\r\n" + 
				"}\r\n" + 
				"@inproceedings{anagnostopoulos2016object,\r\n" + 
				"  title={Object Boundaries and Room Detection in As-Is BIM Models from Point Cloud Data},\r\n" + 
				"  author={Anagnostopoulos, Ioannis and Belsky, Michael and Brilakis, Ioannis},\r\n" + 
				"  booktitle={Proceedings of the 16th International Conference on Computing in Civil and Building Engineering, Osaka, Japan},\r\n" + 
				"  pages={6--8},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{https://www.researchgate.net/profile/Ioannis_Brilakis/publication/305303330_Object_Boundaries_and_Room_Detection_in_As-Is_BIM_Models_from_Point_Cloud_Data/links/5787556d08aeac8561de0b87.pdf}},\r\n" + 
				"  citations={1}\r\n" + 
				"}\r\n" + 
				"@article{liu2016ontology,\r\n" + 
				"  title={Ontology-Based Representation and Reasoning in Building Construction Cost Estimation in China},\r\n" + 
				"  author={Liu, Xin and Li, Zhongfu and Jiang, Shaohua},\r\n" + 
				"  journal={Future Internet},\r\n" + 
				"  volume={8},\r\n" + 
				"  number={3},\r\n" + 
				"  pages={39},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={Multidisciplinary Digital Publishing Institute},\r\n" + 
				"  howpublished = {\\url{http://www.mdpi.com/1999-5903/8/3/39htm}},\r\n" + 
				"  citations={0}\r\n" + 
				"}\r\n" + 
				"@article{bernal2016parametric,\r\n" + 
				"  title={From Parametric to Meta Modeling in Design},\r\n" + 
				"  author={Bernal, Marcelo},\r\n" + 
				"  journal={Blucher Design Proceedings},\r\n" + 
				"  volume={3},\r\n" + 
				"  number={1},\r\n" + 
				"  pages={579--583},\r\n" + 
				"  year={2016},\r\n" + 
				"  howpublished = {\\url{http://pdf.blucher.com.br/designproceedings/sigradi2016/815.pdf}},\r\n" + 
				"  citations={0}\r\n" + 
				"}\r\n" + 
				"@inproceedings{lee2016augmented,\r\n" + 
				"  title={Augmented reality for ndimensional building information modelling: Contextualization, Customization and Curation},\r\n" + 
				"  author={Lee, Xia Sheng and Khamidi, Mohd Faris and See, Zi Siang and Lees, Tim John and Chai, Changsaar},\r\n" + 
				"  booktitle={Virtual System \\& Multimedia (VSMM), 2016 22nd International Conference on},\r\n" + 
				"  pages={1--5},\r\n" + 
				"  year={2016},\r\n" + 
				"  organization={IEEE},\r\n" + 
				"  howpublished = {\\url{http://ieeexplore.ieee.org/abstract/document/7863152/}},\r\n" + 
				"  citations={0}\r\n" + 
				"}\r\n" + 
				"@article{slusarczyk2016ontology,\r\n" + 
				"  title={An ontology-based graph approach to support buildings design conformity with a given style},\r\n" + 
				"  author={{\\'S}lusarczyk, Gra{\\.z}yna and Strug, Barbara and Stasiak, Karolina},\r\n" + 
				"  journal={Applied Ontology},\r\n" + 
				"  volume={11},\r\n" + 
				"  number={4},\r\n" + 
				"  pages={279--300},\r\n" + 
				"  year={2016},\r\n" + 
				"  publisher={IOS Press},\r\n" + 
				"  howpublished = {\\url{https://content.iospress.com/articles/applied-ontology/ao172}},\r\n" + 
				"  citations={0}\r\n" + 
				"}\r\n" + 
				"";
		Reader reader = new StringReader(bib);
		BibTeXParser p = new BibTeXParser();
		BibtexTooling bt = new BibtexTooling();
		BibTeXDatabase db1 = p.parse(reader);
		BibTeXDatabase db2 = db1;
		BibTeXDatabase dbRes = bt.mergeToConsole(db1, db2);
		boolean areEqual = true;
		boolean hasDuplicates = false;
		Map<Key, BibTeXEntry> db1Entries = db1.getEntries();
		Map<Key, BibTeXEntry> db2Entries = db1.getEntries();
		Map<Key, BibTeXEntry> dbResEntries = db1.getEntries();
		Set<BibTeXEntry> dbResEntrySet = new HashSet<BibTeXEntry>(dbResEntries.values());
		Collection<BibTeXEntry> allEntries = new HashSet<BibTeXEntry>(dbResEntries.values());
		allEntries.addAll(db1Entries.values());
		allEntries.addAll(db2Entries.values());

		if(dbResEntrySet.size() != dbResEntries.size()) {
			hasDuplicates = true;
		}
		
		for(BibTeXEntry en : allEntries) {
			if( !(db1.getEntries().containsValue(en) && db2.getEntries().containsValue(en) && dbRes.getEntries().containsValue(en)) ) {
				areEqual = false;
				break;
			}
		}
		assertTrue(areEqual);
		assertFalse(hasDuplicates);
	}

}
