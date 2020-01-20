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

import java.io.File

import opennlp.tools.util.Span
import opennlp.tools.namefind.{TokenNameFinderModel, NameFinderME}
import opennlp.tools.sentdetect.{SentenceModel, SentenceDetectorME}
import opennlp.tools.tokenize.{Tokenizer, SimpleTokenizer}

import com.scleradb.config.ScleraConfig

/** Named Entity Extractor based on Apache OpenNLP */
class OpenNlpExtractor(langOpt: Option[String]) {
    /** Language. Taken to be English, if not specified */
    private val lang: String = langOpt.getOrElse("en").toLowerCase

    /** Named entity */
    class Entity(
        val entityType: String, val start: Int, val end: Int, val text: String
    )

    /** Location of the models */
    private val modelHome: File =
        new File(ScleraConfig.serviceAssetDir, "opennlp")
    modelHome.mkdirs()

    /** Named entity finder, based on pre-build models */
    private def finder(id: String): NameFinderME = {
        val modelf: File = modelFile(
            s"$lang-ner-${id.toLowerCase}.bin", "the model for \"" + id + "\""
        )

        try new NameFinderME(new TokenNameFinderModel(modelf))
        catch { case (e: Throwable) =>
            throw new IllegalArgumentException(
                "Could not load the model for \"" + id + "\".\n" +
                "Reason: " + e.getMessage(),
                e
            )
        }
    }

    /** Sentence detector, based on pre-build models */
    private lazy val sentenceDetector: SentenceDetectorME = {
        val modelf: File = modelFile(
            s"$lang-sent.bin", "the sentence detector model"
        )

        try new SentenceDetectorME(new SentenceModel(modelf))
        catch { case (e: Throwable) =>
            throw new IllegalArgumentException(
                "Could not load the sentence detector.\n" +
                "Reason: " + e.getMessage(),
                e
            )
        }
    }

    /** Extract sentences */
    private def sentences(text: String): Array[String] =
        sentenceDetector.sentDetect(text)

    /** Text tokenizer */
    private val tokenizer: Tokenizer = SimpleTokenizer.INSTANCE

    /** Tokenize text */
    private def tokens(text: String): Array[String] = tokenizer.tokenize(text)

    /** Extract the entities from the text
      * @param finderIds Identifiers of the extractors
      * @param text Input text
      * @return List of extracted entities
      */
    def extract(
        finderIds: List[String],
        text: String
    ): List[Entity] = {
        val finders: List[NameFinderME] = finderIds.map(finder)

        sentences(text).toList.flatMap { sentence =>
            val ts: Array[String] = tokens(sentence)

            finders.flatMap { finder =>
                finder.find(ts).map { span =>
                    val entity: String =
                        ts.slice(span.getStart, span.getEnd).mkString(" ")

                    new Entity(span.getType, span.getStart, span.getEnd, entity)
                }
            }
        }
    }

    private def modelFile(modelFileName: String, modelDesc: String): File = {
        val file: File = new File(modelHome, modelFileName)
        if( !file.exists ) {
            throw new IllegalArgumentException(
                "Could not find " + modelDesc + ".\n" +
                "Please download the model " + "(check " +
                "http://opennlp.sourceforge.net/models-1.5/" + modelFileName +
                "), or create one using Apache OpenNLP " +
                "(see http://opennlp.apache.org), " +
                "and place at " + file.getAbsolutePath()
            )
        }

        file
    }
}
