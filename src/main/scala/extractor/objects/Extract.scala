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

package com.scleradb.plugin.analytics.nlp.opennlp.extractor.objects

import com.scleradb.sql.expr.{ScalExpr, ColRef, CharConst}
import com.scleradb.sql.result.TableResult

import com.scleradb.analytics.nlp.objects.NlpTask

import com.scleradb.plugin.analytics.nlp.opennlp.extractor.eval.ExtractResult

/** Named-entity extraction from text
  * @param langOpt Language code, optional
  * @param args Operator arguments
  * @param inputCol Input column containing the text
  * @param resultColsSpec Output columns containg the operator results
  */
case class Extract(
    langOpt: Option[String],
    args: List[ScalExpr],
    override val inputCol: ColRef,
    resultColsSpec: List[ColRef]
) extends NlpTask {
    override val name: String = "EXTRACT"

    /** Identifiers of the extractors */
    val extractorIds: List[String] = args.map {
        case CharConst(s) => s
        case other => 
            throw new IllegalArgumentException(
                "Found invalid extractor id \"" + other.repr +
                "\" (expect a string)"
            )
    }

    // Sanity check
    if( resultColsSpec.size > 2 ) {
        throw new IllegalArgumentException(
            "Found extra extractor result columns: " +
            resultColsSpec.drop(2).map(
                col => "\"" + col.repr + "\""
            ).mkString(", ")
        )
    }

    /** Output entity column */
    val entityCol: ColRef = 
        resultColsSpec.lift(0) getOrElse ColRef(inputCol.name + "_" + "ENTITY")
    /** Output label column */
    val labelCol: ColRef =
        resultColsSpec.lift(1) getOrElse ColRef(inputCol.name + "_" + "LABEL")

    override val resultCols: List[ColRef] = List(entityCol, labelCol)

    override def eval(rs: TableResult): ExtractResult = new ExtractResult(
        langOpt, extractorIds, inputCol, entityCol, labelCol, rs
    )
}
