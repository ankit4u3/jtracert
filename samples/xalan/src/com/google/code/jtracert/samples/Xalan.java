package com.google.code.jtracert.samples;

import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

public class Xalan {

    public static void main(String argv[])
          throws TransformerException, TransformerConfigurationException, IOException, SAXException,
            ParserConfigurationException, FileNotFoundException
  {
    // Set the TransformerFactory system property to generate and use a translet.
    // Note: To make this sample more flexible, load properties from a properties file.
    // The setting for the Xalan Transformer is "org.apache.xalan.processor.TransformerFactoryImpl"
    String key = "javax.xml.transform.TransformerFactory";
    String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
    Properties props = System.getProperties();
    props.put(key, value);
    System.setProperties(props);

    String xslInURI = "resources/todo.xsl";
    String xmlInURI = "resources/todo.xml";
    String htmlOutURI = "resources/todo.html";
    try
    {
      // Instantiate the TransformerFactory, and use it along with a SteamSource
      // XSL stylesheet to create a Transformer.
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer(new StreamSource(xslInURI));
      // Perform the transformation from a StreamSource to a StreamResult;
      transformer.transform(new StreamSource(xmlInURI),
                            new StreamResult(new FileOutputStream(htmlOutURI)));
      System.out.println("Produced resources/todo.html");
    }
    catch (Exception e)
    {
     System.out.println(e.toString());
     e.printStackTrace();
    }
  }

}
