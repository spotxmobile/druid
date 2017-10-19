/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.druid.data.input.processors;

import io.druid.data.input.MapBasedInputRow;

/**
 *
 */
public class SpotXProcessorCopyWhen implements ISpotXMapInputRowProcessor
{
  private final String copyTo;
  private final String copyFrom;

  private final String whenField;
  private final String hasValue;

  public SpotXProcessorCopyWhen(String copyFrom, String copyTo, String whenField, String hasValue) {

    this.copyTo = copyTo;
    this.copyFrom = copyFrom;
    this.whenField = whenField;
    this.hasValue = hasValue;
  }

  public MapBasedInputRow process(MapBasedInputRow row)
  {
    Object whenFieldValue = row.getRaw(whenField);
    Object copyFromValue = row.getRaw(copyFrom);

    if(whenFieldValue != null &&
       copyFromValue != null ) {

      if (whenFieldValue.equals(hasValue)) {

        row.getEvent().put(copyTo, copyFromValue);
      }
    }

    return row;
  }
}
