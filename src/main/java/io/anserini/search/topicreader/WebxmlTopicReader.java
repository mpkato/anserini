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
import java.util.List;

import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.dom.DOMMarkupParser;
import org.attoparser.dom.Document;
import org.attoparser.dom.Element;
import org.attoparser.dom.IDOMMarkupParser;
import org.attoparser.dom.Text;

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

    String doc = loadFile(bRdr);
    IDOMMarkupParser parser = new DOMMarkupParser(ParseConfiguration.htmlConfiguration());
    try {
      Document document = parser.parse(doc);
      Element queriesNode = document.getFirstChildOfType(Element.class);
      List<Element> queryNodes = queriesNode.getChildrenOfType(Element.class);
      for (Element queryNode: queryNodes) {
        Map<String, String> fields = new HashMap<>();
        String number = queryNode.getAttributeValue("number");
        Map<String, String> rawFields = extractFields(queryNode);
        fields.put("title", rawFields.get("query"));
        map.put(Integer.valueOf(number), fields);
      }
    } catch (ParseException e) {
      System.out.print(e);
    }

    return map;
  }

}
