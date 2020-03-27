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

package com.scleradb.plugin.analytics.nlp.opennlp.extractor.eval

import com.scleradb.sql.types.SqlCharVarying
import com.scleradb.sql.expr.{ColRef, CharConst, ScalValueBase, SortExpr}
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.{TableResult, ExtendedTableRow}

/** Extraction result
  * @param langOpt Language code, optional
  * @param extractorIds Identifiers of the extractors
  * @param inputCol Input column containing the text
  * @param entityCol Output entity column
  * @param labelCol Output label column
  * @param rs Input intermediate result
  */
class ExtractResult(
    langOpt: Option[String],
    extractorIds: List[String],
    inputCol: ColRef,
    entityCol: ColRef,
    labelCol: ColRef,
    rs: TableResult
) extends TableResult {
    override val columns: List[Column] =
        Column(entityCol.name, SqlCharVarying(None))::
        Column(labelCol.name, SqlCharVarying(None))::
        rs.columns

    private val openNlpExtractor: OpenNlpExtractor = OpenNlpExtractor(langOpt)
    
    override def rows: Iterator[ExtendedTableRow] = rs.typedRows.flatMap { t =>
        val text: String = t.getStringOpt(inputCol.name) getOrElse {
            throw new IllegalArgumentException(
                "Column \"" + inputCol.repr + "\" not found"
            )
        }

        openNlpExtractor.extract(extractorIds, text).map { entity =>
            val resultMap: Map[String, CharConst] = Map(
                entityCol.name -> CharConst(entity.text),
                labelCol.name -> CharConst(entity.entityType.toUpperCase)
            )

            ExtendedTableRow(t, resultMap)
        }
    }

    override val resultOrder: List[SortExpr] = rs.resultOrder

    override def close(): Unit = { }
}

/** Companion object */
object ExtractResult {
    /** Constructor */
    def apply(
        langOpt: Option[String], extractorIds: List[String],
        inputCol: ColRef, entityCol: ColRef, labelCol: ColRef, rs: TableResult
    ): ExtractResult = new ExtractResult(
        langOpt, extractorIds, inputCol, entityCol, labelCol, rs
    )
}
