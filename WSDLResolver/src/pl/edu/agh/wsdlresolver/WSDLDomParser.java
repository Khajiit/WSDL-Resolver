package pl.edu.agh.wsdlresolver;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class WSDLDomParser{

    public static void main (String argv []){
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("UserService.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " + 
                 doc.getDocumentElement().getNodeName());

            NodeList listOfOperations = doc.getElementsByTagName("wsdl:operation");
            int totalOperations = listOfOperations.getLength();
            System.out.println("Total no of operations : " + listOfOperations.getLength());
            System.out.println("WebMethods:");
            //moj test
            for(int s=0; s<listOfOperations.getLength() ; s++){
            	

                Node operationNode = listOfOperations.item(s);
                if(operationNode.getNodeType() == Node.ELEMENT_NODE){


                    Element operationElement = (Element)operationNode;
                    
                    System.out.println("    " + operationElement.getAttribute("name"));
//
//                    //-------
//                    NodeList firstNameList = operationElement.getElementsByTagName("first");
//                    Element firstNameElement = (Element)firstNameList.item(0);
//
//                    NodeList textFNList = firstNameElement.getChildNodes();
//                    System.out.println("First Name : " + 
//                           ((Node)textFNList.item(0)).getNodeValue().trim());
//
//                    //-------
//                    NodeList lastNameList = operationElement.getElementsByTagName("last");
//                    Element lastNameElement = (Element)lastNameList.item(0);
//
//                    NodeList textLNList = lastNameElement.getChildNodes();
//                    System.out.println("Last Name : " + 
//                           ((Node)textLNList.item(0)).getNodeValue().trim());
//
//                    //----
//                    NodeList ageList = operationElement.getElementsByTagName("age");
//                    Element ageElement = (Element)ageList.item(0);
//
//                    NodeList textAgeList = ageElement.getChildNodes();
//                    System.out.println("Age : " + 
//                           ((Node)textAgeList.item(0)).getNodeValue().trim());
//
//                    //------


                }//end of if clause


            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        //System.exit (0);

    }//end of main


}