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

import org.attoparser.dom.Element;
import org.attoparser.dom.Text;

/**
 * Topic reader for the XML format.
 */
public abstract class XmlTopicReader<T> extends TopicReader<T> {
  public XmlTopicReader(Path topicFile) {
    super(topicFile);
  }

  String loadFile(BufferedReader bRdr) {
    /**
     * Read String from BufferedReader
     */
    try {
      StringBuilder builder = new StringBuilder();

      int c;
      while ((c = bRdr.read()) != -1) {
        builder.append((char) c);
      }

      String doc = builder.toString();

      return doc;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  Map<String, String> extractFields(Element element) {
    /**
     * Extract elements in an element
     */
    Map<String, String> fields = new HashMap<>();
    List<Element> children = element.getChildrenOfType(Element.class);
    for (Element child: children) {
      String name = child.getElementName();
      Text text = child.getFirstChildOfType(Text.class);
      String content = text.getContent().trim();
      fields.put(name, content);
    }
    return fields;
  }

}

