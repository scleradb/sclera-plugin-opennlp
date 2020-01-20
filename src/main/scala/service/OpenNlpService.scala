/**
* Sclera - OpenNLP Connector
* Copyright 2012 - 2020 Sclera, Inc.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.scleradb.plugin.analytics.nlp.opennlp.service

import com.scleradb.sql.expr.{ScalExpr, ColRef}

import com.scleradb.analytics.nlp.service.NlpService
import com.scleradb.analytics.nlp.expr.NlpRelOp

import com.scleradb.plugin.analytics.nlp.opennlp.extractor.objects.Extract

/** Apache OpenNLP Connector service */
class OpenNlpService extends NlpService {
    override val id: String = "OPENNLP"

    override def createObject(
        langOpt: Option[String],
        opName: String,
        args: List[ScalExpr],
        inputCol: ColRef,
        resultCols: List[ColRef]
    ): Extract = opName.toUpperCase match {
        case "EXTRACT" =>
            Extract(langOpt, args, inputCol, resultCols)
        case _ =>
            throw new IllegalArgumentException(
                "Operation \"" + opName +
                "\" is not supported by module \"" + id + "\""
            )
    }
}
