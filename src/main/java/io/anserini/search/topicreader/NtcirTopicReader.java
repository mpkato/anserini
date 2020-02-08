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
 * Topic reader for the XML format used in the NTCIR We Want Web (WWW) Tracks.
 * Please cite the following papers if the NTCIR WWW test collections are used
 * in a publication.
 * <p>
 * http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings13/pdf/ntcir/01-NTCIR13-OV-WWW-LuoC.pdf
 * http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings14/pdf/ntcir/01-NTCIR14-OV-WWW-MaoJ.pdf
 */
public class NtcirTopicReader extends XmlTopicReader<String> {
  public NtcirTopicReader(Path topicFile) {
    super(topicFile);
  }

  @Override
  public SortedMap<String, Map<String, String>> read(BufferedReader bRdr) throws IOException {
    /**
     * There are no narratives in NTCIR WWW topics, so this method returns
     * a map whose keys are description and title only.
     *
     * Example:
     * <queries>
     *   <query>
     *     <qid>0001</qid>
     *     <content>Halloween picture</content>
     *     <description>Halloween is coming. ...</description>
     *   </query>
     * </queries>
     */

    SortedMap<String, Map<String, String>> map = new TreeMap<>();

    Document document = loadFile(bRdr);
    Element root = document.getDocumentElement();
    NodeList queryNodeList = root.getChildNodes();
    for (int i = 0; i < queryNodeList.getLength(); i++) {
      Node queryNode = queryNodeList.item(i);
      if (queryNode.getNodeType() != Node.ELEMENT_NODE)
        continue;
      Element query = (Element) queryNode;
      Map<String, String> fields = new HashMap<>();
      Map<String, String> rawFields = extractFields(query);
      fields.put("title", rawFields.get("query"));
      fields.put("title", rawFields.get("content"));
      fields.put("description", rawFields.get("description"));
      if (rawFields.containsKey("qid")) {
        map.put(rawFields.get("qid"), fields);
      }
    }

    return map;
  }

}
