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
package io.druid.data.input.parsers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.avro.generic.GenericRecord;

import java.util.ArrayList;
import java.util.List;

import io.druid.data.input.AvroHadoopInputRowParser;
import io.druid.data.input.InputRow;
import io.druid.data.input.MapBasedInputRow;
import io.druid.data.input.processors.ISpotXMapInputRowProcessor;
//import io.druid.data.input.processors.SpotXProcessorApplyRegEx;
//import io.druid.data.input.processors.SpotXProcessorCopyWhen;
import io.druid.data.input.processors.SpotXProcessorScaleBy;
import io.druid.data.input.impl.ParseSpec;


/**
 *
 */
public class SpotXForecastIRPHadoop extends AvroHadoopInputRowParser
{
  private final List<ISpotXMapInputRowProcessor> processors;

  @JsonCreator
  public SpotXForecastIRPHadoop(
      @JsonProperty("parseSpec") ParseSpec parseSpec,
      @JsonProperty("fromPigAvroStorage") Boolean fromPigAvroStorage
  )
  {
    super(parseSpec, fromPigAvroStorage);

    processors = new ArrayList<>();

    //scale calls by sample rate
    processors.add(
        new SpotXProcessorScaleBy(
            "met_calls",
            "met_sample_rate"
        )
    );

    //scale impressions by sample rate
    processors.add(
        new SpotXProcessorScaleBy(
            "met_impressions",
            "met_sample_rate"
        )
    );

/*
    //extract host from url
    processors.add(
        new SpotXProcessorApplyRegEx(
            "dim_host",
            "/^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n]+)/im"
        )
    );

    //copy cpi to impression cpi if impression true
    processors.add(
        new SpotXProcessorCopyWhen(
            "met_clearance_cpi_min",
            "met_clearance_impression_cpi",
            "dim_impression",
            "1"
        )
    );
*/

  }

  @Override
  public InputRow parse(GenericRecord input)
  {
    MapBasedInputRow output = (MapBasedInputRow) super.parse(input);

    for(ISpotXMapInputRowProcessor processor : processors) {

      output = processor.process(output);
    }

    return output;
  }
}
