package net.tjeerd.camt053parser;

import net.tjeerd.camt053parser.model.*;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Camt053Parser {

    /**
     * Parse a CAMT.053 formatted bank statement from the given input stream.
     *
     * @param inputStream input stream containing the CAMT.053 formatted bank statement
     * @return document holding CAMT.053 parsed bank statement
     * @throws JAXBException
     */
    public Document parse(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Document camt053Document = ((JAXBElement<Document>) unmarshaller.unmarshal(inputStream)).getValue();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "camt053.xml");
        //marshaller.marshal(camt053Document, System.out);

        return camt053Document;
    }
}
