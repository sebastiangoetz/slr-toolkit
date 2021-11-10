package de.davidtiede.slrtoolkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import de.davidtiede.slrtoolkit.database.Taxonomy;

public class TaxonomyParser {
    public boolean isEmpty(String string) {
        if(string != null && !string.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<TaxonomyParserNode> parse(String taxonomy) {
        char openingBracet = "{".charAt(0);
        char closingBracet = "}".charAt(0);
        char comma = ",".charAt(0);
        String node = "";
        Stack<TaxonomyParserNode> parentNodes = new Stack<>();
        List<TaxonomyParserNode> taxonomyNodes = new ArrayList<>();

        for(int i = 0; i < taxonomy.length(); i++) {
            if(Character.compare(taxonomy.charAt(i), openingBracet) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    parentNodes.push(taxonomyNode);
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else if(Character.compare(taxonomy.charAt(i), closingBracet) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    parentNodes.pop();
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else if(Character.compare(taxonomy.charAt(i), comma) == 0) {
                if(!isEmpty(node)) {
                    String trimmedNode = node.trim();
                    //System.out.println(trimmedNode);
                    TaxonomyParserNode taxonomyNode = new TaxonomyParserNode();
                    taxonomyNode.setName(trimmedNode);
                    if(parentNodes.size() > 0) {
                        taxonomyNode.setParent(parentNodes.peek());
                    }
                    taxonomyNodes.add(taxonomyNode);
                    node = "";
                }
            } else {
                node += taxonomy.charAt(i);
            }
        }
        System.out.println(taxonomyNodes.size());
        return taxonomyNodes;
    }

    public void test() {
        String testTaxonomy = "Zielgruppe {\n" +
                "\tunspezifisch,\n" +
                "\tPrivatsektor{\n" +
                "\tunspezifisch,\n" +
                "\tEigentuemer,\n" +
                "\tProjektmanager,\n" +
                "\tAEC-Hintergrund,\n" +
                "\tFacility Manager,\n" +
                "\tIT-Hintergrund\n" +
                "},\n" +
                "\tOeffentlicher Sektor{\n" +
                "\tRegierung,\n" +
                "\tStudenten und Professoren,\n" +
                "\tOeffentliche Auftraggeber\n" +
                "\t}\n" +
                "},\n" +
                "\n" +
                "Veroeffentlichungsland {\n" +
                "\t\tNordamerika{\n" +
                "\t\tVereinigte Staaten von Amerika,\t\n" +
                "\t\tKanada\n" +
                "\t\t},\n" +
                "\t\tEuropa{\n" +
                "\t\tVereinigtes Koenigreich,\n" +
                "\t\tSlovenien,\n" +
                "\t\tNiederlande,\n" +
                "\t\tIrland,\n" +
                "\t\tSpanien,\n" +
                "\t\tDeutschland,\n" +
                "\t\tNorwegen,\n" +
                "\t\tRumaenien,\n" +
                "\t\tFinnland,\n" +
                "\t\tItalien\n" +
                "\t\t},\n" +
                "\t\tAsien{\n" +
                "\t\tIndien,\n" +
                "\t\tSuedkorea,\n" +
                "\t\tIsrael,\n" +
                "\t\tSaudi-Arabien,\n" +
                "\t\tIndonesien,\n" +
                "\t\tMalaisia,\n" +
                "\t\tChina\n" +
                "\t\t},\n" +
                "\n" +
                "\t\tNeuseeland,\n" +
                "\t\tAustralien\n" +
                "\n" +
                "\t},\n" +
                "Veroeffentlichungssort {\n" +
                "\t \tJohn Wiley and Sons Inc,\n" +
                "\t \tSYBEX Inc,\n" +
                "\t \tInternational Smart Cities Conference,\n" +
                "\t \tJournal of Information Technology in Construction,\n" +
                "\t \tInternational Conference on 3D Web Technology,\n" +
                "\t \tAutomation in Construction,\n" +
                "\t \tConstruction Lawyer,\n" +
                "\t \tJournal of Management in Engineering,\n" +
                "\t \tASHRAE Journal,\n" +
                "\t \tPortland International Center for Management of Engineering and Technology,\n" +
                "\t \tInternational Conference on Smart City and Systems Engineering,\n" +
                "\t \tTransmission and Distribution Conference and Exposition,\n" +
                "\t \tLeadership and Management in Engineering,\n" +
                "\t \tInternational Conference on Information System and Artificial Intelligence,\n" +
                "\t \tRenewable and Sustainable Energy Reviews,\n" +
                "\t \tInternational Conference on Power System Technology,\n" +
                "\t \tInternational Conference on Construction in Developing Countries,\n" +
                "\t\tJournal of Construction Engineering and Management,\n" +
                "\t\tInternational Journal of Project Management,\n" +
                "\t\tJournal of Professional Issues in Engineering Education and Practice,\n" +
                "\t\tJournal of Building Engineering,\n" +
                "\t\tInternational Conference on Virtual Systems and Multimedia,\n" +
                "\t\tIEEE International Conference on Industrial Engineering and Engineering Management,\n" +
                "\t\tInternational Conference on Research and Innovation in Information Systems,\n" +
                "\t\tInternational Conference on Compututational Intelligence and Security,\n" +
                "\t\tWinter Simulation Conference,\n" +
                "\t\tFrontiers in Education Conference ,\n" +
                "\t\tInternational Conference on Intelligent Systems Design and Engineering Applications,\n" +
                "\t\tInternational Conference on Management Science and Engineering,\n" +
                "\t\tInternational Conference on Computer Supported Cooperative Work in Design,\n" +
                "\t\tHawaii International Conference on System Science,\n" +
                "\t\tDigital Heritage International Congress,\n" +
                "\t\tInternational Conference on Smart Grid and Electrical Automation,\n" +
                "\t\tInternational Conference on Optimization of Electrical and Electronic Equipment and,\n" +
                "\t\tInternational Aegean Conference on Electrical Machines and Power Electronics,\n" +
                "\t\tInternational Symposium on Mixed and Augmented Reality \n" +
                "\t},\n" +
                "Art{\n" +
                "\t\tBuch,\n" +
                "\t\tKonferenz,\n" +
                "\t\tFachzeitschrift\n" +
                "\t},\n" +
                "Thema{\n" +
                "\t\tAblaufplanung,\n" +
                "\t\tInformationsberechnung und Praesentation,\n" +
                "\t\tGebaeudeanalyse Nachhaltigkeit und Energieverbrauch,\n" +
                "\t\tGebaeudesicherheit und Fehlermanagement,\n" +
                "\t\tInteroperabilitaet BIM und Lean Construction,\n" +
                "\t\tKommunikation und Interaktion,\n" +
                "\t\tInnovative Forschungsthemen und Trends,\t\n" +
                "\t\tAnwendung von BIM auf intelligente Gebaeude,\n" +
                "\t\tLuecke zwischen CAD und 3D Modellierung,\n" +
                "\t\tAnwendung von BIM Praktiken und Software in zukuenftigen Projekten,\n" +
                "\t\tVeraenderter Lehrfokus und Lernerfahrung durch BIM,\n" +
                "\t\tAnwendung von BIM auf bereits existierende Gebaeude,\n" +
                "\t\tEchtzeitvisualisierung mit BIM und Augmented Reality,\n" +
                "\t\tUebersicht und Anleitung zu BIM,\t\n" +
                "\t\tQualitative und finanzielle Vorteile von BIM\n" +
                "},\n" +
                "\t\n" +
                "Anwendungsfeld{\n" +
                "\t\tunspezifisch,\n" +
                "\t\tLehre,\n" +
                "\t\tOrganisation,\n" +
                "\t\tFinanzierung,\n" +
                "\t\tAEC{\n" +
                "\t\t\tVorkonstruktion,\n" +
                "\t\t\tKonstruktion\n" +
                "\t\t},\n" +
                "\t\tFacility Management,\n" +
                "\t\tIT\t\n" +
                "\t},\n" +
                "Ergebnis{\n" +
                "\tpraktisch{\n" +
                "\t\tImplementation,\n" +
                "\t\tFramework,\n" +
                "\t\tBeispiel\n" +
                "\t},\n" +
                "\ttheoretisch{\n" +
                "\t\tUeberblick,\n" +
                "\t\tArbeitsfluss,\n" +
                "\t\tAnalyse,\n" +
                "\t\tBericht\n" +
                "\t\t\n" +
                "\t}\n" +
                "\t},\n" +
                "\t\n" +
                "Methodik{Interviews,\n" +
                "\t\tEntwurf,\n" +
                "\t\tFallstudie,\n" +
                "\t\tArgumentation,\n" +
                "\t\tExperiment,\n" +
                "\t\tUmfrage,\n" +
                "\t\tDiskussion,\n" +
                "\t\tLiteraturrezension,\n" +
                "\t\tForschungsworkshop,\n" +
                "\t\tAnalyse,\n" +
                "\t\tBericht\n" +
                "\t\n" +
                "}\n";
        parse(testTaxonomy);
    }
}
