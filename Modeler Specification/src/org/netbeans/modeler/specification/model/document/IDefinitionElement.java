/**
 * Copyright [2014] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.modeler.specification.model.document;

/**
 * IDefinitionElement contains multiple IRootElement (e.g jBatchSuite , JBMPN (subprocess in process) ) 
 * IRootElement contains IBaseElement children
 * <definition-element>
 * <root-element>
 * <child-element>...</child-element><child-element>...</child-element><child-element>...</child-element>
 * <diagram-element>...</diagram-element>
 * </root-element>
 * <root-element>
 * <child-element>...</child-element><child-element>...</child-element><child-element>...</child-element>
 * <diagram-element>...</diagram-element>
 * </root-element>
 * <root-element>
 * <child-element>...</child-element><child-element>...</child-element><child-element>...</child-element>
 * <diagram-element>...</diagram-element>
 * </root-element>
 * </definition-element>
 *
 * @author Gaurav Gupta
 */
public interface IDefinitionElement {

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getId();

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setId(String value);

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName();

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value);
}
