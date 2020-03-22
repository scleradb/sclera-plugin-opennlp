# Sclera - OpenNLP Connector

Enables Sclera to perform text analytics on free-form text.

Current version of this component only supports extracting entities (such as names of persons and places, dates, emails) from the text. Later versions will include additional features such as sentiment/opinion mining.

The entity extraction is exposed as a SQL operator (Sclera's extension) which can act on any relational input. The operator is given the name of the column containing the text data, and the output is the input will additional columns containing the extracted information. The output can then be aggregated, joined with other tables, etc. as usual within the SQL query.

This component uses the [Apache OpenNLP](http://opennlp.apache.org) library, which is downloaded automatically as a part of the installation.

To use this component, you will also need to provide Sclera with trained models for a sentence detector and name finders (extractors) for your language. These are not packaged with Sclera, but can be downloaded separately from the [Apache OpenNLP models repository](http://opennlp.sourceforge.net/models-1.5/). The site provides models in Danish (code: `da`), German (code: `de`), English (code: `en`), Dutch (code: `dl`), Portuguese (code: `pt`) and Swedish (code: `se`). The models files can be downloaded from the site and kept in the directory `<sclera-assets>/opennlp`, where `<sclera-assets>` is the directory given by the [`sclera.services.assetdir` configuration parameter](https://scleradb.com/docs/setup/configuration/#sclera-services-assetdir).

For greater accuracy on your data, you can also [create your own name finders using Apache OpenNLP's toolkit](http://opennlp.apache.org/documentation/1.5.3/manual/opennlp.html#tools.namefind.training).

Please refer to the [ScleraSQL Reference](https://scleradb.com/docs/sclerasql/sqlexttext/#sclera-opennlp) document for details on using the component's features in a SQL query.
