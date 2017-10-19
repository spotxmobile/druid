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

public class SpotXProcessorScaleBy implements ISpotXMapInputRowProcessor
{
  private final String toScale;
  private final String scaleBy;

  public SpotXProcessorScaleBy(String toScale, String scaleBy) {

    this.toScale = toScale;
    this.scaleBy = scaleBy;
  }

  public MapBasedInputRow process(MapBasedInputRow row) {

      if(row.getRaw(toScale) != null) {

        double multiplier = row.getFloatMetric(scaleBy);

        if( multiplier <= 0.0 ) { multiplier = 100; }

        multiplier = 1.0 / (multiplier / 100);

        row.getEvent().replace(toScale, row.getFloatMetric(toScale) * multiplier);}

      return row;
  }

}
