/**
 * 
 */
package tirnav.passman.xml.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

/**
 * @author jainj
 *
 */
public class JsonConverter<T> {

	private final Class<T> documentClass;
    private final JsonMapper mapper;
    
    public JsonConverter(Class<T> documentClass) {
        this.documentClass = documentClass;
        this.mapper = new JsonMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
	/**
     * Maps the given object to the given output stream.
     *
     * @param document the document object which represents the XML document
     * @param outputStream the output stream
     * @throws IOException if any error occurred
     */
    public void write(T document, OutputStream outputStream) throws IOException {
        mapper.writeValue(outputStream, document);
    }
    
    /**
     * Maps the given object to the given output stream.
     *
     * @param document the document object which represents the XML document
     * @param outputStream the output stream
     * @throws IOException if any error occurred
     */
    public void writeCSV(T document, OutputStream outputStream) throws IOException {
    	mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
    	mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
    	String json = mapper.writeValueAsString(document);
    	JsonNode jsonTree = mapper.readTree(json).elements().next();
    	final Builder csvSchemaBuilder = CsvSchema.builder(); 
    	JsonNode firstObject = jsonTree.elements().next();
    	firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
    	CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
    	CsvMapper csvMapper = new CsvMapper();
    	csvMapper.writerFor(JsonNode.class)
    	  .with(csvSchema)
    	  .writeValue(outputStream, jsonTree);
    }
    
}
