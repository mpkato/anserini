/**
 * Anserini: A Lucene toolkit for replicable information retrieval research
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.anserini.search.topicreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;

import java.nio.file.Paths;
import java.io.InputStream;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Topic reader for the XML format.
 */
public abstract class XmlTopicReader<T> extends TopicReader<T> {
  public XmlTopicReader(Path topicFile) {
    super(topicFile);
  }

  Document loadFile(BufferedReader bRdr) {
    /**
     * Read Document from BufferedReader
     */
    Document document = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(new InputSource(bRdr));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } 
    return document;
  }

  Map<String, String> extractFields(Element element) {
    /**
     * Extract elements in an element
     */
    Map<String, String> fields = new HashMap<>();
    NodeList children = element.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node childNode = children.item(i);
      if (childNode.getNodeType() != Node.ELEMENT_NODE)
        continue;
      Element child = (Element) childNode;
      String name = child.getTagName();
      String content = child.getTextContent().trim();
      fields.put(name, content);
    }
    return fields;
  }

}

