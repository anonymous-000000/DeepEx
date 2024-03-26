package xml;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import evaluation.APICallSpec;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import static xml.MatchingType.*;
import static xml.XMLUtil.asList;

public class XMLQuery {
    private final DocumentBuilderFactory factory;
    private final DocumentBuilder builder;
    private final XPathFactory xpfactory;
    private final XPath xpath;
    private final String expression = "/methods/method";
    private List<String> allExceptions = new ArrayList<>();
    private Path log;

    private final String[] xmlFileNames = {
            "./output/joda-time.xml",
            "./output/gwt.xml",
            "./output/hibernate-orm.xml",
            "./output/jdk.xml",
            "./output/xstream.xml",
            "./output/android.xml"
    };

    public XMLQuery(Properties config) throws Exception{
        this.factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        factory.setIgnoringElementContentWhitespace(true);

        this.xpfactory = XPathFactory.newInstance();
        this.xpath = xpfactory.newXPath();

        allExceptions.addAll(Files.readAllLines(Path.of(config.getProperty("all_exceptions"))));
    }

    public List<Set<String>> predExceptions(String name, String arityX) throws IOException, SAXException {
        // the method will return a random set from this list
        List<Set<String>> PSetCandidates = new ArrayList<>();

        for (String filename : xmlFileNames) {
            Document doc = builder.parse(filename);
            try {
                XPathExpression expr = xpath.compile(expression);
                NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node n = nodes.item(i);
                    if (n.getAttributes().getNamedItem("name").getNodeValue().equals(name)) {
                        Set<String> exceptions = getExceptions(((DeferredElementImpl)((DeferredElementImpl) n.getChildNodes()).getElementsByTagName("exceptions").item(0).getChildNodes()).getElementsByTagName("e"));

                        // fetch arity
                        Node arityNode = ((DeferredElementImpl) n.getChildNodes()).getElementsByTagName("arity").item(0);
                        String arity = arityNode.getFirstChild().getNodeValue();
                        if (Integer.valueOf(arity) == Integer.valueOf(arityX))
                            PSetCandidates.add(exceptions);
                    }
                }
            } catch (XPathExpressionException e)
            {
                System.out.println(e.getMessage());
            }
        }

        return PSetCandidates;
    }

    /*
     * Return True if the API call has a matched try-block in the databases
     * */
    public MatchingType process(APICallSpec apiCallSpec) throws Exception {
        for (String filename : xmlFileNames) {
            Document doc = builder.parse(filename);
            try {
                XPathExpression expr = xpath.compile(expression);
                NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node n = nodes.item(i);
                    if (n.getAttributes().getNamedItem("name").getNodeValue().equals(apiCallSpec.methodName)) {
                        // check whether at least one exception is from the `allExceptions` list
                        if (!isValid(((DeferredElementImpl)((DeferredElementImpl) n.getChildNodes()).getElementsByTagName("exceptions").item(0).getChildNodes()).getElementsByTagName("e"))) {
                            continue;
                        }

                        // fetch arity
                        Node arityNode = ((DeferredElementImpl) n.getChildNodes()).getElementsByTagName("arity").item(0);
                        String arity = arityNode.getFirstChild().getNodeValue();
                        if (Integer.valueOf(arity) == apiCallSpec.arity)
                            return MATCH;
                    }
                }
            } catch (XPathExpressionException e)
            {
                System.out.println(e.getMessage());
            }
        }

        return NO_MATCH;
    }

    private Set<String> getExceptions(NodeList eNodes) throws IOException {
        Set<String> exceptions = new HashSet<>();

        for(Node n: asList(eNodes)) {
            String exception = n.getFirstChild().getNodeValue();
            if (exception.contains(".")) {
                try {
                    exception = exception.trim().split("\\.")[exception.split("\\.").length - 1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }


            if (allExceptions.contains(exception)) {
                exceptions.add(exception);
            }
        }

        return exceptions;
    }

    private boolean isValid(NodeList eNodes) throws IOException {
        String exception;
        for(Node n: asList(eNodes)) {
            exception = n.getFirstChild().getNodeValue();
            if (exception.contains(".")) {
                try {
                    exception = exception.trim().split("\\.")[exception.split("\\.").length - 1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }

            assert(exception.length() > 2);

            if (allExceptions.contains(exception)) {
                return true;
            }
        }

        return false;
    }

    public static String description(Node n)
    {
        if (n instanceof Element) return "Element " + n.getNodeName();
        else if (n instanceof Attr) return "Attribute " + n;
        else return n.toString();
    }
}
