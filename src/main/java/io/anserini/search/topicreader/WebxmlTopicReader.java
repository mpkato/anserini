/**
 * Anserini: A Lucene toolkit for replicable information retrieval research
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.anserini.search.topicreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Topic reader for standard XML format used in the TREC Web Tracks.
 */
public class WebxmlTopicReader extends XmlTopicReader<Integer> {
  public WebxmlTopicReader(Path topicFile) {
    super(topicFile);
  }

  @Override
  public SortedMap<Integer, Map<String, String>> read(BufferedReader bRdr) throws IOException {
    SortedMap<Integer, Map<String, String>> map = new TreeMap<>();

    Document document = loadFile(bRdr);
    Element root = document.getDocumentElement();
    NodeList queryNodeList = root.getChildNodes();
    for (int i = 0; i < queryNodeList.getLength(); i++) {
      Node queryNode = queryNodeList.item(i);
      if (queryNode.getNodeType() != Node.ELEMENT_NODE)
        continue;
      Element query = (Element) queryNode;
      Map<String, String> fields = new HashMap<>();
      String number = query.getAttribute("number");
      Map<String, String> rawFields = extractFields(query);
      fields.put("title", rawFields.get("query"));
      map.put(Integer.valueOf(number), fields);
    }
    return map;
  }

}
