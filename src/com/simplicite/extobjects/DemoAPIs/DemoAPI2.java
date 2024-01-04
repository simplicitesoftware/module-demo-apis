package com.simplicite.extobjects.DemoAPIs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.Globals;
import com.simplicite.util.ModuleDB;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.exceptions.SearchException;
import com.simplicite.util.tools.Parameters;
import com.simplicite.util.tools.HTTPTool;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.JSONTool;

/**
 * Custom REST web services (GET = search/select only)
 */
public class DemoAPI2 extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	private transient JSONObject config = null;

	/**
	 * Externa object initialization: load the JSON configuration stored in the dedicated system parameter
	 */
	@Override
	public void init(Parameters params) {
		config = getGrant().getJSONObjectParameter("DEMO_API2_CONFIG");
	}

	/**
	 * Search/select using URI <code>/api/ext/DemoAPI2/&lt;object name&gt;[?&lt;field name 1&bt;=&lt;filter 1&bt;&amp;&lt;field name 2&gt;=&lt;filter 2&gt;&amp;...|/&lt;unique ID field name&gt;]</code>
	 */
	@Override
	public Object get(Parameters params) throws HTTPException {
		ObjectDB obj = null;
		try {
			List<String> parts = params.getURIParts(getName());

			if (parts.isEmpty())
				return notFound("No object");

			if ("openapi.yml".equals(parts.get(0))) {
				setYAMLMIMEType();
				return JSONTool.getYAMLASCIILogo(null) + JSONTool.toYAML(openapi());
			}

			String objName = parts.get(0);
			if (!config.has(objName))
				return notFound("Unknown object: " + objName);

			obj = borrowAPIObject(objName); // Borrow an API object instance from the pool (ZZZ must be returned, see below)

			if (parts.size() == 1) { // Search
				List<String[]> rows = obj.getTool().search(params.getParameters()); // filtered search

				// Remove standard row IDs
				JSONArray list = new JSONArray(obj.toJSON(rows, null, false, true, false));
				for (int i = 0; i < list.length(); i++)
					list.getJSONObject(i).remove("row_id");

				return list;
			} else { // Select from the custom ID field
				String value = parts.get(1);
				String field = config.getString(objName);
				List<String[]> rows = obj.getTool().search(new JSONObject().put(field, value)); // filtered search using reference only

				// No unique record matching the custom ID field's value => not found
				if (rows.size() != 1)
					return notFound("No " + objName + " for " + field + " = " + value);

				// Remove standard row ID
				JSONObject item = new JSONObject(obj.toJSON(rows.get(0), null, false, true));
				item.remove("row_id");

				return item;
			}
		} catch (SearchException e) {
			return error(e);
		} finally {
			if (obj != null)
				returnAPIObject(obj); // Return the API object intance to the pool
		}
	}

	@Override
	public JSONObject openapi() {
		int[] errs = new int[] { 400, 401, 500 };

		JSONObject schemas = new JSONObject();
		//JSONTool.addOpenAPIErrorsSchemas(schemas, errs, JSONTool.OPENAPI_OAS3);

		JSONObject paths = new JSONObject();
		JSONTool.addLoginOpenAPIPath(paths, JSONTool.OPENAPI_OAS3);
		JSONObject nameParam = JSONTool.addOpenAPIParameterType(new JSONObject()
			.put("name", "name")
			.put("description", "Object name")
			.put("required", true)
			.put("in", "path"), "string", null, JSONTool.OPENAPI_OAS3);
		JSONObject idParam = JSONTool.addOpenAPIParameterType(new JSONObject()
			.put("name", "id")
			.put("description", "Object row ID")
			.put("required", true)
			.put("in", "path"), "string", null, JSONTool.OPENAPI_OAS3);
		paths.put("/{name}", new JSONObject()
			.put("get", JSONTool.addOpenAPIOperationType(new JSONObject()
				.put("description", "Object list")
				.put("operationId", "objList")
				.put("parameters", new JSONArray().put(nameParam))
				.put("security", new JSONArray().put(new JSONObject().put("bearerAuth", new JSONArray())))
				.put("responses", new JSONObject().put("200", new JSONObject().put("description", "List"))),
			null, null, HTTPTool.MIME_TYPE_JSON, null, errs, JSONTool.OPENAPI_OAS3)));
		paths.put("/{name}/{id}", new JSONObject()
			.put("get", JSONTool.addOpenAPIOperationType(new JSONObject()
				.put("description", "Object item")
				.put("operationId", "objItem")
				.put("parameters", new JSONArray().put(nameParam).put(idParam))
				.put("security", new JSONArray().put(new JSONObject().put("bearerAuth", new JSONArray())))
				.put("responses", new JSONObject().put("200", new JSONObject().put("description", "Item"))),
			null, null, HTTPTool.MIME_TYPE_JSON, null, errs, JSONTool.OPENAPI_OAS3)));
		JSONTool.addLogoutOpenAPIPath(paths, JSONTool.OPENAPI_OAS3);

		JSONObject info = new JSONObject()
			.put("title", HTMLTool.toPlainText(getDisplay()))
			.put("description", HTMLTool.toPlainMarkdownText(getDesc()))
			.put("version", HTMLTool.toPlainText(ModuleDB.getModuleVersionFromId(getModuleId())));

		return JSONTool.getOpenAPISchema(getGrant(), JSONTool.OPENAPI_OAS3, info, null, Globals.WEB_API_PATH + "/" + getName(), paths, schemas, true);
	}
}
